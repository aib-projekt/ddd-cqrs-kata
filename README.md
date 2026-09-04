# Kata: Order / Inventory / Payment — refactoring to DDD + CQRS + hexagonal

# 🚧 Work in progress — see checklist below for current stage

## Domain (mini-PRD)

A simple shop. A customer places an order with at least one line (product +
quantity + unit price). The system must:

1. reserve stock for every line,
2. charge payment for the order total,
3. confirm the order only once both of the above succeed,
4. roll back whatever already succeeded if any step fails (compensation),
5. allow cancelling an order that hasn't been confirmed yet.

This is exactly the "2-3 services" scale (Order, Inventory, Payment) — small
enough to work through in a few evenings, rich enough to give you a real
cross-aggregate consistency problem (i.e. an actual reason to reach for a
saga / domain events, not just an excuse to draw nice packages).

## Starting point (this code)

A layered monolith, with `order`, `inventory`, and `payment` as packages,
each following `Controller -> Service -> Repository -> Entity`. It
intentionally contains the following smells:

- **Anemic model** — `Order`, `InventoryItem`, `Payment` are bare JPA
  entities with getters/setters; all logic (reservation, compensation,
  payment limit) lives in the services.
- **Entity = write model = read model = API model** — controllers return
  JPA entities directly. No CQRS, no output DTOs at all.
- **Direct coupling between contexts** — `OrderService` knows the concrete
  classes `InventoryService` and `PaymentService` and calls them
  synchronously. No ports, no domain events.
- **Hand-rolled compensation** — the `for` loops in
  `OrderService.createOrder` are, in effect, a very fragile, fully
  synchronous version of a choreographed saga, with no step log/audit and
  no handling for "the compensation itself also fails".
- **Deliberate business gap** — `cancelOrder` doesn't release stock or
  refund the payment. That's not a typo — it's something to notice and fix
  as part of the refactoring (a good discussion point about where that rule
  should live once the code is refactored).
- No ArchUnit, nothing enforcing that the domain doesn't depend on Spring.

`OrderServiceTest` is a characterization test — it documents today's
behaviour (smells included). Keep it green while refactoring as a safety
net; if you deliberately change behaviour (e.g. fix `cancelOrder`), update
the test and write down *why*.

## Goal of this exercise

I'm not giving you the solution — you're meant to work it out. Below is a
checklist and a target structure as a reference point, but the actual
refactoring is yours to practice. When you're done (or stuck), paste your
result back and I'll review it the way an interview stress-test would:
why a port here, why an event there, what happens if...

### Checklist

1. **Extract the domain layer** (no Spring/JPA annotations):
   - `Order` as an aggregate: a constructor/factory that enforces
     invariants (at least 1 line, no negative quantities), methods like
     `reserveStockFailed()`, `paymentDeclined()`, `confirm()`, `cancel()`
     instead of public setters called from the service.
   - Value objects: `Money`, `ProductId`, `Quantity` instead of bare
     `BigDecimal`/`String`/`int` (think about where this actually pays off
     vs. where it's over-engineering).
   - Domain events: `OrderCreated`, `StockReservationFailed`,
     `PaymentDeclined`, `OrderConfirmed`, `OrderCancelled`.
2. **CQRS**: separate the *command* side (mutating the aggregate through a
   use case) from the *query* side (a read model optimised for the
   UI/API — this can be a separate projection/DTO built with a direct
   query, without going through the aggregate at all).
3. **Ports and adapters** (per bounded context: `order`, `inventory`,
   `payment`):
   - `domain/` — aggregates, VOs, events, outgoing ports (interfaces like
     `OrderRepository`) — zero Spring/JPA imports.
   - `application/` — command/query handlers (use cases), orchestration.
   - `infrastructure/adapter/in/web/` — controllers + request/response DTOs.
   - `infrastructure/adapter/out/persistence/` — port implementation via
     JPA (JPA entity ≠ domain aggregate, so you'll need a mapper).
4. **Replace the synchronous cross-context calls** with one of two
   approaches (consider both, pick one deliberately, and be able to
   justify it):
   - a saga orchestrator in the Order `application` layer, calling
     `InventoryPort`/`PaymentPort` with an explicit compensation ladder,
   - choreography via domain events + an Outbox (you know this from
     ControlPlane — this is the miniature version).
5. **Fix `cancelOrder`** deliberately — decide whether the compensation is
   a command handler on Order, or a reaction to an `OrderCancelled` event
   in Inventory/Payment, and why.
6. **ArchUnit**: a rule that `..domain..` doesn't depend on
   `org.springframework..` or `jakarta.persistence..`.
7. (Bonus) Make `InventoryRepository`/`OrderRepository` a port with two
   implementations — JPA for writes and a plain JDBC/projection for
   reads — a natural place to practice **jOOQ** on the query side (it's on
   your list for Revolut prep).

### Questions to ask yourself afterwards

- Where exactly is the boundary of the Order aggregate — should
  `OrderLine` be an entity or a VO? What would change if a product had a
  versioned price?
- Which adapter depends on which port, and not the other way round — check
  the direction of the arrows between `application` and `infrastructure`.
- What happens if the compensation step itself fails (e.g. `releaseStock`
  throws after a declined payment)? Where do you detect that, and what do
  you do next?
- Should the query side even know the Order aggregate exists, or should it
  read straight from a table/projection?

## Stretch goal: migrating to Spring Boot 4.1

This project starts on Spring Boot 3.5 (Java 17) — deliberately, since
that's a realistic starting point for a migration you'd actually see at a
real company today. Once you've finished the DDD/CQRS refactoring, it's
worth practicing the migration itself as a separate exercise:

- Spring Boot 4.0 (November 2025, alongside Spring Framework 7) was a
  generational reset: a Jakarta EE 11 baseline, Jackson 3, null-safety via
  JSpecify, split auto-configuration JARs, Gradle 9 support. 3.x lost free
  OSS support in June 2026, so this isn't really optional anymore.
- Spring Boot 4.1 (released June 10, 2026) is incremental on top of 4.0.
  The parts most relevant to your stack:
  - **lazy JDBC connections**
    (`spring.datasource.connection-fetch=lazy`) — the connection is only
    acquired when actually needed rather than at the start of every
    transaction, interesting given your interest in transaction
    propagation,
  - **async context propagation** for `@Async` methods via Micrometer,
  - **SSRF mitigation** (`InetAddressFilter`) for the HTTP client,
  - Spring gRPC auto-configuration (server + client, Netty/Servlet HTTP/2),
  - **jOOQ bumped to 3.20** — the only thing in 4.1 that requires Java 21
    (everything else keeps the 17 baseline) — relevant if you want to
    practice jOOQ on the query side in this same project,
  - Type-Safe Property Paths in Spring Data (no more stringly-typed
    property references).
  - Spring Boot 4.2 is planned for November 2026 — 4.1 gets roughly 13
    months of support, so it's the sensible migration target, not 4.0.

Do the migration as a separate step/branch *after* the architecture
refactoring — otherwise you can't tell version-upgrade bugs apart from
package-restructuring bugs.

## Running it

```bash
mvn spring-boot:run
```

H2 console at `/h2-console` (JDBC URL: `jdbc:h2:mem:shop`). Tests:

```bash
mvn test
```
# ddd-cqrs-kata

### (a) Tradeoffs
When comparing event sourcing with the plain relational adapter in CampusCoffee, the conventional CRUD approach relies on overwriting the current state directly in the database. To handle concurrent modifications safely, a relational adapter often must utilize a **pessimistic locking mechanism** (or optimistic locking) to prevent conflicting updates. Event sourcing sidesteps in-place updates entirely by appending immutable events to a log, guaranteeing that the true history is preserved perfectly without losing previous states.

For what kinds of projects or requirements does event sourcing
justify its added complexity?

Event sourcing justifies its added complexity in projects where we may need
to secure us legally (by keeping track of changes done) or if we want to be able
to reconstruct past states.

---

When does a conventional approach serve better?

It's better when we build a very simple project (where the
history plays no role) or
for example if we only have limited time to
develop a project.

### (b) Use cases
1. We can see who approved a review by inspecting the events table which stores
ReviewApproval events. EventRepository and EventEntity return approver id and
timestamp for entries written by EventSourcedReviewApprovalDataService.
2. EventStore records create, update and delete events for reviews.
EventSourcedReviewDataService and ReadModelProjector let us reconstruct who created,
edited or deleted a review and when.

### (c) Snapshots
As proposed in this exercise, we could periodically make snapshots.
Then we can rebuild from the newest snapshot and only have to replay
the events that come after the snapshot.

# 4.2.1

* [#18](https://github.com/squigglesql/squigglesql/pull/18): Added module-info.

# 4.2.0

* [#15](https://github.com/squigglesql/squigglesql/pull/15): Added DeleteQuery.

# 4.1.0

* :boom: **Breaking change.** Criteria implementations should now be accessed via static methods, e.g. Criteria.equal,
Criteria.not etc. Subclasses are now package-private. This change substantially reduces the amount of code you need to
type, especially if you import static Criteria.*
* [#11](https://github.com/squigglesql/squigglesql/issues/11): Added `JOIN` syntax (see Join, CrossJoin, QualifiedJoin,
QualifiedJoinKind). Added SelectQuery#addFrom method which allows you to add table references and joins to `FROM`
section of the query explicitly. All missing table references still get inferred properly.
* [#12](https://github.com/squigglesql/squigglesql/issues/12): Added Criteria.distinct (`IS DISTINCT FROM`) and
Criteria.notDistinct (`IS NOT DISTINCT FROM`).
* [#10](https://github.com/squigglesql/squigglesql/pull/10): Added [H2](https://www.h2database.com) database support.

# 4.1.0

* :boom: **Breaking change.** Criteria implementations should now be accessed via static methods, e.g. Criteria.equal,
Criteria.not etc. Subclasses are now package-private. This change substantially reduces the amount of code you need to
type, especially if you import static Criteria.*
* Added `JOIN` syntax (see Join, CrossJoin, QualifiedJoin, QualifiedJoinKind).
* Added SelectQuery#addFrom method which allows you to add table references and joins to `FROM` section of the query
explicitly. All missing table references still get inferred properly.
* Added Criteria.distinct (`IS DISTINCT FROM`) and Criteria.notDistinct (`IS NOT DISTINCT FROM`).

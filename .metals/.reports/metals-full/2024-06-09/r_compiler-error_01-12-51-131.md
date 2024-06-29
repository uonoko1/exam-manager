file:///C:/Users/sakai/Developments/exam-manager/test/infrastructrue/db/repository/ExamResultRepositoryImplSpec.scala
### file%3A%2F%2F%2FC%3A%2FUsers%2Fsakai%2FDevelopments%2Fexam-manager%2Ftest%2Finfrastructrue%2Fdb%2Frepository%2FExamResultRepositoryImplSpec.scala:57: error: unclosed quoted identifier
            dbConfig.db.run(ExamResultTable.examResults P`)
                                                         ^

occurred in the presentation compiler.

presentation compiler configuration:
Scala version: 2.12.19
Classpath:
<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\scala-library\2.12.19\scala-library-2.12.19.jar [exists ]
Options:



action parameters:
uri: file:///C:/Users/sakai/Developments/exam-manager/test/infrastructrue/db/repository/ExamResultRepositoryImplSpec.scala
text:
```scala
package infrastructure.db.repository

import org.scalatestplus.play._
import org.scalatest.concurrent.ScalaFuture
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.db.slick.DatabaseConfigProvider
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.{Application, inject}
import infrastructure.db.DatabaseConfig
import infrastructure.db.tables.{ExamResult, ExamResultTable}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import play.api.test.Injecting

class ExamResultRepositoryImplSpec extends AnyWordSpec with Matchers with ScalaFutures with BeforeAndAfterEach with GuiceOneAppPerSuite with Injecting {
    override def fakeApplication(): Application = {
        new GuiceApplicationBuilder()
            .configure("slick.dbs.default.profile" -> "slick.jdbc.H2Profile$")
            .configure("slick.dbs.default.db.driver" -> "org.h2.Driver")
            .configure("slick.dbs.default.db.url" -> "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1")
            .configure("slick.dbs.default.db.user" -> "sa")
            .configure("slick.dbs.default.db.password" -> "")
            .build()
    }

    val dbConfigProvider = inject.instanceOf[DatabaseConfigProvider]
    val dbConfig = new DatabaseConfig(dbConfigProvider)
    val repository = new ExamResultRepositoryImpl(dbConfig)

    override def beforeEach(): Unit = {
        import dbCondig.profile.api._
        dbConfig.db.run(ExamResultTable.examResults.schema.create).futureValue
    }

    override def afterEach(): Unit = {
        import dbCondig.profile.api._
        dbConfig.db.run(ExamResultTable.examResults.schema.drop).futureValue
    }

    "ExamResultRepositoryImpl#save" should {
        "save an exam result" in {
            val examResult = ExamResult("11111111111111111111", "Math", 85, 1)
            val saveFuture = repository.save(examResult)
            whenReady(saveFuture) {_ =>
                import dbConfig.profile.api._
                val query = ExamResultTable.examResults.filter(_.examId === examResult.exaId)
                val result = dbConfig.db.run(query.resuly.headOption).futureValue
                result mustBe Some(examResult)
            }
        }
    }

    "ExamResultRepositoryImpl#findById" should {
        "find an exam result by ID" in {
            val examResult = ExamResult("11111111111111111111", "Math", 85, 1)
            dbConfig.db.run(ExamResultTable.examResults P`)
        }
    }
}
```



#### Error stacktrace:

```
scala.meta.internal.tokenizers.Reporter.syntaxError(Reporter.scala:23)
	scala.meta.internal.tokenizers.Reporter.syntaxError$(Reporter.scala:23)
	scala.meta.internal.tokenizers.Reporter$$anon$1.syntaxError(Reporter.scala:33)
	scala.meta.internal.tokenizers.Reporter.syntaxError(Reporter.scala:25)
	scala.meta.internal.tokenizers.Reporter.syntaxError$(Reporter.scala:25)
	scala.meta.internal.tokenizers.Reporter$$anon$1.syntaxError(Reporter.scala:33)
	scala.meta.internal.tokenizers.LegacyScanner.getBackquotedIdent(LegacyScanner.scala:496)
	scala.meta.internal.tokenizers.LegacyScanner.fetchToken(LegacyScanner.scala:344)
	scala.meta.internal.tokenizers.LegacyScanner.nextToken(LegacyScanner.scala:214)
	scala.meta.internal.tokenizers.LegacyScanner.foreach(LegacyScanner.scala:982)
	scala.meta.internal.tokenizers.ScalametaTokenizer.uncachedTokenize(ScalametaTokenizer.scala:23)
	scala.meta.internal.tokenizers.ScalametaTokenizer.$anonfun$tokenize$1(ScalametaTokenizer.scala:16)
	scala.collection.concurrent.TrieMap.getOrElseUpdate(TrieMap.scala:895)
	scala.meta.internal.tokenizers.ScalametaTokenizer.tokenize(ScalametaTokenizer.scala:16)
	scala.meta.internal.tokenizers.ScalametaTokenizer$$anon$2.apply(ScalametaTokenizer.scala:331)
	scala.meta.tokenizers.Api$XtensionTokenizeDialectInput.tokenize(Api.scala:25)
	scala.meta.tokenizers.Api$XtensionTokenizeInputLike.tokenize(Api.scala:14)
	scala.meta.internal.parsers.ScannerTokens$.apply(ScannerTokens.scala:994)
	scala.meta.internal.parsers.ScalametaParser.<init>(ScalametaParser.scala:33)
	scala.meta.parsers.Parse$$anon$1.apply(Parse.scala:35)
	scala.meta.parsers.Api$XtensionParseDialectInput.parse(Api.scala:25)
	scala.meta.internal.semanticdb.scalac.ParseOps$XtensionCompilationUnitSource.toSource(ParseOps.scala:17)
	scala.meta.internal.semanticdb.scalac.TextDocumentOps$XtensionCompilationUnitDocument.toTextDocument(TextDocumentOps.scala:206)
	scala.meta.internal.pc.SemanticdbTextDocumentProvider.textDocument(SemanticdbTextDocumentProvider.scala:54)
	scala.meta.internal.pc.ScalaPresentationCompiler.$anonfun$semanticdbTextDocument$1(ScalaPresentationCompiler.scala:403)
```
#### Short summary: 

file%3A%2F%2F%2FC%3A%2FUsers%2Fsakai%2FDevelopments%2Fexam-manager%2Ftest%2Finfrastructrue%2Fdb%2Frepository%2FExamResultRepositoryImplSpec.scala:57: error: unclosed quoted identifier
            dbConfig.db.run(ExamResultTable.examResults P`)
                                                         ^
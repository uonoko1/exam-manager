file:///C:/Users/sakai/Developments/exam-manager/test/infrastructrue/db/repository/ExamResultRepositoryImplSpec.scala
### file%3A%2F%2F%2FC%3A%2FUsers%2Fsakai%2FDevelopments%2Fexam-manager%2Ftest%2Finfrastructrue%2Fdb%2Frepository%2FExamResultRepositoryImplSpec.scala:59: error: declaration requires a type
        }
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
            dbConfig.db.run(ExamResultTable.examResults += examResult).futureValue
            val findFuture
        }
    }
}
```



#### Error stacktrace:

```
scala.meta.internal.parsers.Reporter.syntaxError(Reporter.scala:16)
	scala.meta.internal.parsers.Reporter.syntaxError$(Reporter.scala:16)
	scala.meta.internal.parsers.Reporter$$anon$1.syntaxError(Reporter.scala:22)
	scala.meta.internal.parsers.Reporter.syntaxError(Reporter.scala:17)
	scala.meta.internal.parsers.Reporter.syntaxError$(Reporter.scala:17)
	scala.meta.internal.parsers.Reporter$$anon$1.syntaxError(Reporter.scala:22)
	scala.meta.internal.parsers.ScalametaParser.$anonfun$patDefOrDcl$7(ScalametaParser.scala:3702)
	scala.Option.fold(Option.scala:251)
	scala.meta.internal.parsers.ScalametaParser.$anonfun$patDefOrDcl$1(ScalametaParser.scala:3702)
	scala.meta.internal.parsers.ScalametaParser.autoEndPos(ScalametaParser.scala:368)
	scala.meta.internal.parsers.ScalametaParser.autoEndPos(ScalametaParser.scala:373)
	scala.meta.internal.parsers.ScalametaParser.patDefOrDcl(ScalametaParser.scala:3675)
	scala.meta.internal.parsers.ScalametaParser.defOrDclOrSecondaryCtor(ScalametaParser.scala:3637)
	scala.meta.internal.parsers.ScalametaParser.localDef(ScalametaParser.scala:4635)
	scala.meta.internal.parsers.ScalametaParser.iter$7(ScalametaParser.scala:4672)
	scala.meta.internal.parsers.ScalametaParser.$anonfun$blockStatSeq$1(ScalametaParser.scala:4695)
	scala.meta.internal.parsers.ScalametaParser.$anonfun$blockStatSeq$1$adapted(ScalametaParser.scala:4645)
	scala.meta.internal.parsers.ScalametaParser.scala$meta$internal$parsers$ScalametaParser$$listBy(ScalametaParser.scala:565)
	scala.meta.internal.parsers.ScalametaParser.blockStatSeq(ScalametaParser.scala:4645)
	scala.meta.internal.parsers.ScalametaParser.blockWithinDelims(ScalametaParser.scala:2574)
	scala.meta.internal.parsers.ScalametaParser.$anonfun$blockInDelims$2(ScalametaParser.scala:2577)
	scala.meta.internal.parsers.ScalametaParser.inBracesOnOpen(ScalametaParser.scala:263)
	scala.meta.internal.parsers.ScalametaParser.$anonfun$blockOnBrace$1(ScalametaParser.scala:2588)
	scala.meta.internal.parsers.ScalametaParser.$anonfun$blockInDelims$1(ScalametaParser.scala:2577)
	scala.meta.internal.parsers.ScalametaParser.atPos(ScalametaParser.scala:316)
	scala.meta.internal.parsers.ScalametaParser.autoPos(ScalametaParser.scala:365)
	scala.meta.internal.parsers.ScalametaParser.blockInDelims(ScalametaParser.scala:2577)
	scala.meta.internal.parsers.ScalametaParser.blockOnBrace(ScalametaParser.scala:2588)
	scala.meta.internal.parsers.ScalametaParser.$anonfun$blockExprOnBrace$1(ScalametaParser.scala:2591)
	scala.meta.internal.parsers.ScalametaParser.blockExprPartial(ScalametaParser.scala:2570)
	scala.meta.internal.parsers.ScalametaParser.blockExprOnBrace(ScalametaParser.scala:2590)
	scala.meta.internal.parsers.ScalametaParser.argumentExprsOrPrefixExpr(ScalametaParser.scala:2494)
	scala.meta.internal.parsers.ScalametaParser.getNextRhs$2(ScalametaParser.scala:2113)
	scala.meta.internal.parsers.ScalametaParser.getPostfixOrNextRhs$1(ScalametaParser.scala:2147)
	scala.meta.internal.parsers.ScalametaParser.loop$6(ScalametaParser.scala:2167)
	scala.meta.internal.parsers.ScalametaParser.postfixExpr(ScalametaParser.scala:2192)
	scala.meta.internal.parsers.ScalametaParser.postfixExpr(ScalametaParser.scala:2087)
	scala.meta.internal.parsers.ScalametaParser.$anonfun$expr$2(ScalametaParser.scala:1682)
	scala.meta.internal.parsers.ScalametaParser.atPosOpt(ScalametaParser.scala:319)
	scala.meta.internal.parsers.ScalametaParser.autoPosOpt(ScalametaParser.scala:366)
	scala.meta.internal.parsers.ScalametaParser.expr(ScalametaParser.scala:1587)
	scala.meta.internal.parsers.ScalametaParser.$anonfun$blockStatSeq$3(ScalametaParser.scala:4681)
	scala.meta.internal.parsers.ScalametaParser.stat(ScalametaParser.scala:4518)
	scala.meta.internal.parsers.ScalametaParser.iter$7(ScalametaParser.scala:4681)
	scala.meta.internal.parsers.ScalametaParser.$anonfun$blockStatSeq$1(ScalametaParser.scala:4695)
	scala.meta.internal.parsers.ScalametaParser.$anonfun$blockStatSeq$1$adapted(ScalametaParser.scala:4645)
	scala.meta.internal.parsers.ScalametaParser.scala$meta$internal$parsers$ScalametaParser$$listBy(ScalametaParser.scala:565)
	scala.meta.internal.parsers.ScalametaParser.blockStatSeq(ScalametaParser.scala:4645)
	scala.meta.internal.parsers.ScalametaParser.blockWithinDelims(ScalametaParser.scala:2574)
	scala.meta.internal.parsers.ScalametaParser.$anonfun$blockInDelims$2(ScalametaParser.scala:2577)
	scala.meta.internal.parsers.ScalametaParser.inBracesOnOpen(ScalametaParser.scala:263)
	scala.meta.internal.parsers.ScalametaParser.$anonfun$blockOnBrace$1(ScalametaParser.scala:2588)
	scala.meta.internal.parsers.ScalametaParser.$anonfun$blockInDelims$1(ScalametaParser.scala:2577)
	scala.meta.internal.parsers.ScalametaParser.atPos(ScalametaParser.scala:316)
	scala.meta.internal.parsers.ScalametaParser.autoPos(ScalametaParser.scala:365)
	scala.meta.internal.parsers.ScalametaParser.blockInDelims(ScalametaParser.scala:2577)
	scala.meta.internal.parsers.ScalametaParser.blockOnBrace(ScalametaParser.scala:2588)
	scala.meta.internal.parsers.ScalametaParser.$anonfun$blockExprOnBrace$1(ScalametaParser.scala:2591)
	scala.meta.internal.parsers.ScalametaParser.blockExprPartial(ScalametaParser.scala:2570)
	scala.meta.internal.parsers.ScalametaParser.blockExprOnBrace(ScalametaParser.scala:2590)
	scala.meta.internal.parsers.ScalametaParser.argumentExprsOrPrefixExpr(ScalametaParser.scala:2494)
	scala.meta.internal.parsers.ScalametaParser.getNextRhs$2(ScalametaParser.scala:2113)
	scala.meta.internal.parsers.ScalametaParser.getPostfixOrNextRhs$1(ScalametaParser.scala:2147)
	scala.meta.internal.parsers.ScalametaParser.loop$6(ScalametaParser.scala:2167)
	scala.meta.internal.parsers.ScalametaParser.postfixExpr(ScalametaParser.scala:2192)
	scala.meta.internal.parsers.ScalametaParser.postfixExpr(ScalametaParser.scala:2087)
	scala.meta.internal.parsers.ScalametaParser.$anonfun$expr$2(ScalametaParser.scala:1682)
	scala.meta.internal.parsers.ScalametaParser.atPosOpt(ScalametaParser.scala:319)
	scala.meta.internal.parsers.ScalametaParser.autoPosOpt(ScalametaParser.scala:366)
	scala.meta.internal.parsers.ScalametaParser.expr(ScalametaParser.scala:1587)
	scala.meta.internal.parsers.ScalametaParser$$anonfun$templateStat$1.applyOrElse(ScalametaParser.scala:4600)
	scala.meta.internal.parsers.ScalametaParser$$anonfun$templateStat$1.applyOrElse(ScalametaParser.scala:4586)
	scala.PartialFunction.$anonfun$runWith$1$adapted(PartialFunction.scala:145)
	scala.meta.internal.parsers.ScalametaParser.statSeqBuf(ScalametaParser.scala:4537)
	scala.meta.internal.parsers.ScalametaParser.getStats$2(ScalametaParser.scala:4576)
	scala.meta.internal.parsers.ScalametaParser.$anonfun$scala$meta$internal$parsers$ScalametaParser$$templateStatSeq$3(ScalametaParser.scala:4577)
	scala.meta.internal.parsers.ScalametaParser.$anonfun$scala$meta$internal$parsers$ScalametaParser$$templateStatSeq$3$adapted(ScalametaParser.scala:4574)
	scala.meta.internal.parsers.ScalametaParser.scala$meta$internal$parsers$ScalametaParser$$listBy(ScalametaParser.scala:565)
	scala.meta.internal.parsers.ScalametaParser.scala$meta$internal$parsers$ScalametaParser$$templateStatSeq(ScalametaParser.scala:4574)
	scala.meta.internal.parsers.ScalametaParser.scala$meta$internal$parsers$ScalametaParser$$templateStatSeq(ScalametaParser.scala:4566)
	scala.meta.internal.parsers.ScalametaParser.$anonfun$templateBody$1(ScalametaParser.scala:4419)
	scala.meta.internal.parsers.ScalametaParser.inBracesOr(ScalametaParser.scala:259)
	scala.meta.internal.parsers.ScalametaParser.inBraces(ScalametaParser.scala:255)
	scala.meta.internal.parsers.ScalametaParser.templateBody(ScalametaParser.scala:4419)
	scala.meta.internal.parsers.ScalametaParser.templateBodyOpt(ScalametaParser.scala:4423)
	scala.meta.internal.parsers.ScalametaParser.templateAfterExtends(ScalametaParser.scala:4366)
	scala.meta.internal.parsers.ScalametaParser.$anonfun$template$1(ScalametaParser.scala:4387)
	scala.meta.internal.parsers.ScalametaParser.atPos(ScalametaParser.scala:316)
	scala.meta.internal.parsers.ScalametaParser.autoPos(ScalametaParser.scala:365)
	scala.meta.internal.parsers.ScalametaParser.template(ScalametaParser.scala:4374)
	scala.meta.internal.parsers.ScalametaParser.$anonfun$templateOpt$1(ScalametaParser.scala:4412)
	scala.meta.internal.parsers.ScalametaParser.atPos(ScalametaParser.scala:316)
	scala.meta.internal.parsers.ScalametaParser.autoPos(ScalametaParser.scala:365)
	scala.meta.internal.parsers.ScalametaParser.templateOpt(ScalametaParser.scala:4404)
	scala.meta.internal.parsers.ScalametaParser.$anonfun$classDef$1(ScalametaParser.scala:4036)
	scala.meta.internal.parsers.ScalametaParser.autoEndPos(ScalametaParser.scala:368)
	scala.meta.internal.parsers.ScalametaParser.autoEndPos(ScalametaParser.scala:373)
	scala.meta.internal.parsers.ScalametaParser.classDef(ScalametaParser.scala:4012)
	scala.meta.internal.parsers.ScalametaParser.tmplDef(ScalametaParser.scala:3971)
	scala.meta.internal.parsers.ScalametaParser.topLevelTmplDef(ScalametaParser.scala:3956)
	scala.meta.internal.parsers.ScalametaParser$$anonfun$2.applyOrElse(ScalametaParser.scala:4558)
	scala.meta.internal.parsers.ScalametaParser$$anonfun$2.applyOrElse(ScalametaParser.scala:4546)
	scala.PartialFunction.$anonfun$runWith$1$adapted(PartialFunction.scala:145)
	scala.meta.internal.parsers.ScalametaParser.statSeqBuf(ScalametaParser.scala:4537)
	scala.meta.internal.parsers.ScalametaParser.bracelessPackageStats$1(ScalametaParser.scala:4756)
	scala.meta.internal.parsers.ScalametaParser.$anonfun$batchSource$11(ScalametaParser.scala:4767)
	scala.meta.internal.parsers.ScalametaParser.autoEndPos(ScalametaParser.scala:368)
	scala.meta.internal.parsers.ScalametaParser.$anonfun$batchSource$10(ScalametaParser.scala:4767)
	scala.meta.internal.parsers.ScalametaParser.tryParse(ScalametaParser.scala:216)
	scala.meta.internal.parsers.ScalametaParser.$anonfun$batchSource$1(ScalametaParser.scala:4759)
	scala.meta.internal.parsers.ScalametaParser.atPos(ScalametaParser.scala:316)
	scala.meta.internal.parsers.ScalametaParser.autoPos(ScalametaParser.scala:365)
	scala.meta.internal.parsers.ScalametaParser.batchSource(ScalametaParser.scala:4727)
	scala.meta.internal.parsers.ScalametaParser.$anonfun$source$1(ScalametaParser.scala:4720)
	scala.meta.internal.parsers.ScalametaParser.atPos(ScalametaParser.scala:316)
	scala.meta.internal.parsers.ScalametaParser.autoPos(ScalametaParser.scala:365)
	scala.meta.internal.parsers.ScalametaParser.source(ScalametaParser.scala:4720)
	scala.meta.internal.parsers.ScalametaParser.entrypointSource(ScalametaParser.scala:4725)
	scala.meta.internal.parsers.ScalametaParser.parseSourceImpl(ScalametaParser.scala:135)
	scala.meta.internal.parsers.ScalametaParser.$anonfun$parseSource$1(ScalametaParser.scala:132)
	scala.meta.internal.parsers.ScalametaParser.parseRuleAfterBOF(ScalametaParser.scala:59)
	scala.meta.internal.parsers.ScalametaParser.parseRule(ScalametaParser.scala:54)
	scala.meta.internal.parsers.ScalametaParser.parseSource(ScalametaParser.scala:132)
	scala.meta.parsers.Parse$.$anonfun$parseSource$1(Parse.scala:29)
	scala.meta.parsers.Parse$$anon$1.apply(Parse.scala:36)
	scala.meta.parsers.Api$XtensionParseDialectInput.parse(Api.scala:25)
	scala.meta.internal.semanticdb.scalac.ParseOps$XtensionCompilationUnitSource.toSource(ParseOps.scala:17)
	scala.meta.internal.semanticdb.scalac.TextDocumentOps$XtensionCompilationUnitDocument.toTextDocument(TextDocumentOps.scala:206)
	scala.meta.internal.pc.SemanticdbTextDocumentProvider.textDocument(SemanticdbTextDocumentProvider.scala:54)
	scala.meta.internal.pc.ScalaPresentationCompiler.$anonfun$semanticdbTextDocument$1(ScalaPresentationCompiler.scala:403)
```
#### Short summary: 

file%3A%2F%2F%2FC%3A%2FUsers%2Fsakai%2FDevelopments%2Fexam-manager%2Ftest%2Finfrastructrue%2Fdb%2Frepository%2FExamResultRepositoryImplSpec.scala:59: error: declaration requires a type
        }
        ^
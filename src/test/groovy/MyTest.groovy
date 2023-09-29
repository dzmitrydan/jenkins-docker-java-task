import com.lesfurets.jenkins.unit.declarative.DeclarativePipelineTest
import org.junit.Before
import org.junit.Test

class MyTest extends DeclarativePipelineTest {

    @Before
    @Override
    void setUp() throws Exception {
        super.setUp()
        helper.registerAllowedMethod('git', [Map], stringInterceptor)
        helper.registerAllowedMethod('codeNarc', [Map], stringInterceptor)
        helper.registerAllowedMethod('recordIssues', [Map], stringInterceptor)
        helper.registerAllowedMethod('junit', [Map], stringInterceptor)
        helper.registerAllowedMethod("parameters", [List])
        helper.registerAllowedMethod('buildAddUrl', [Map], stringInterceptor)
        helper.registerAllowedMethod('addDeployToDashboard', [Map], stringInterceptor)
    }

    @Test
    void checkExecutingWithoutErrors() throws Exception {
        runScript('Test')
        printCallStack()
        assertJobStatusSuccess()
    }
}

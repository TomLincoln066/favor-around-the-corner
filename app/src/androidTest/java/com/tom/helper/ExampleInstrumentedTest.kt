package com.tom.helper

import androidx.lifecycle.LiveData
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tom.helper.homefragment.HomeViewModel
import com.tom.helper.source.DefaultHelperRepository
import com.tom.helper.source.Task
import kotlinx.coroutines.runBlocking

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(MockitoJUnitRunner::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.tom.helper", appContext.packageName)
    }


    @Mock
    lateinit var  repository: DefaultHelperRepository

    lateinit var  viewModel:HomeViewModel

    private lateinit var tasks: LiveData<List<Task>>





    @Before
    fun setup() {

        runBlocking {
            Mockito.`when`(repository.getTasks()).thenReturn(com.tom.helper.source.Result.Success(listOf()))
        }
        viewModel = HomeViewModel(repository)
        tasks = viewModel.tasks
    }


    @Test
    fun getArticles_isEmpty() {
        viewModel.getTasksResult()
        val result = viewModel.getDisplayTimePass(test =60000L)
        assertEquals("1分鐘前","${result}")


        tasks.value?.let {
            assertNotNull(it)

        }
    }


    @Test
    fun getArticles_isFail() {

        runBlocking {
            Mockito.`when`(repository.getTasks()).thenReturn(com.tom.helper.source.Result.Fail("hihi"))
        }

        viewModel.getTasksResult()

        assertNull(tasks.value)
    }




}

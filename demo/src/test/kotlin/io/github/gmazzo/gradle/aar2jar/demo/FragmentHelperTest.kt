package io.github.gmazzo.gradle.aar2jar.demo

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class FragmentHelperTest {

    @Test
    fun testCreateFragmentFactory() {
        assertNotNull(FragmentHelper.createFragmentFactory())
    }

}

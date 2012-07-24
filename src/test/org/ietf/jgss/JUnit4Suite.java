package org.ietf.jgss;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import org.ietf.jgss.OidTest;

@RunWith(Suite.class)
@SuiteClasses({OidTest.class, 
               MessagePropTest.class,
               ChannelBindingTest.class,
               GSSNameTest.class})
public class JUnit4Suite { }


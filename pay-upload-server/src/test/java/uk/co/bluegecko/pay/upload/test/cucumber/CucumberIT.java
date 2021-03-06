package uk.co.bluegecko.pay.upload.test.cucumber;


import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.SnippetType;
import cucumber.api.junit.Cucumber;


@RunWith( Cucumber.class )
@CucumberOptions( plugin =
	{ "pretty", "html:target/cucumber" }, snippets = SnippetType.CAMELCASE,
		glue = "uk.co.bluegecko.pay.upload.test.cucumber.steps", strict = true )
public class CucumberIT
{}

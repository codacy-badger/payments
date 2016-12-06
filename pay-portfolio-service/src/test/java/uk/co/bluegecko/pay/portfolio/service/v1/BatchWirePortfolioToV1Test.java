package uk.co.bluegecko.pay.portfolio.service.v1;


import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import uk.co.bluegecko.pay.portfolio.model.Batch;
import uk.co.bluegecko.pay.portfolio.model.Portfolio;
import uk.co.bluegecko.pay.portfolio.model.base.BatchBase;
import uk.co.bluegecko.pay.portfolio.model.base.PortfolioBase;
import uk.co.bluegecko.pay.test.data.TestConstants;
import uk.co.bluegecko.pay.test.harness.TestHarness;


public class BatchWirePortfolioToV1Test extends TestHarness implements TestConstants
{

	private uk.co.bluegecko.pay.v1.portfolio.wire.Batch.BatchBuilder wireBuilder;
	private uk.co.bluegecko.pay.v1.portfolio.wire.Batch wireBatch;

	private BatchWirePortfolioToV1 wireService;

	@Before
	public final void setUp()
	{
		wireBuilder = uk.co.bluegecko.pay.v1.portfolio.wire.Batch.builder();

		wireService = new BatchWirePortfolioToV1();
	}

	@Test
	public final void testToWire()
	{
		final Portfolio portfolio = new PortfolioBase( PORTFOLIO_ID ).name( PORTFOLIO_NAME )
				.serialNo( SERIAL_NO )
				.userNumber( SUN );

		final Batch batch = new BatchBase( BATCH_ID ).portfolio( portfolio )
				.index( BATCH_IDX )
				.name( BATCH_NAME )
				.userNumber( SUN )
				.set( SET )
				.generation( GENERATION )
				.section( SECTION )
				.sequence( SEQUENCE )
				.version( VERSION );

		wireBatch = wireService.toWire( batch );

		assertThat( wireBatch.id(), is( batch.id() ) );
		assertThat( wireBatch.portfolio(), is( batch.portfolio()
				.get()
				.id() ) );
		assertThat( wireBatch.index(), is( batch.index() ) );
		assertThat( wireBatch.name(), is( batch.name() ) );
		assertThat( wireBatch.userNumber(), is( batch.userNumber() ) );
		assertThat( wireBatch.generation(), is( batch.generation() ) );
		assertThat( wireBatch.section(), is( batch.section() ) );
		assertThat( wireBatch.sequence(), is( batch.sequence() ) );
		assertThat( wireBatch.set(), is( batch.set() ) );
		assertThat( wireBatch.version(), is( batch.version() ) );
	}

	@Test
	public final void testToWireNoPortfolio()
	{
		final Batch batch = new BatchBase( BATCH_ID ).index( BATCH_IDX )
				.name( BATCH_NAME )
				.userNumber( SUN )
				.set( SET )
				.generation( GENERATION )
				.section( SECTION )
				.sequence( SEQUENCE )
				.version( VERSION );

		wireBatch = wireService.toWire( batch );

		assertThat( wireBatch.id(), is( batch.id() ) );
		assertThat( wireBatch.portfolio(), is( nullValue() ) );
		assertThat( wireBatch.index(), is( batch.index() ) );
		assertThat( wireBatch.name(), is( batch.name() ) );
		assertThat( wireBatch.userNumber(), is( batch.userNumber() ) );
		assertThat( wireBatch.generation(), is( batch.generation() ) );
		assertThat( wireBatch.section(), is( batch.section() ) );
		assertThat( wireBatch.sequence(), is( batch.sequence() ) );
		assertThat( wireBatch.set(), is( batch.set() ) );
		assertThat( wireBatch.version(), is( batch.version() ) );
	}

	@Test
	public final void testFromWire()
	{
		wireBatch = wireBuilder.id( BATCH_ID )
				.portfolio( PORTFOLIO_ID )
				.index( BATCH_IDX )
				.name( BATCH_NAME )
				.set( SET )
				.generation( GENERATION )
				.section( SECTION )
				.sequence( SEQUENCE )
				.version( VERSION )
				.build();

		final Batch batch = wireService.fromWire( wireBatch );

		assertThat( batch.id(), is( wireBatch.id() ) );
		assertThat( batch.portfolio()
				.get()
				.id(), is( wireBatch.portfolio() ) );
		assertThat( wireBatch.index(), is( batch.index() ) );
		assertThat( wireBatch.name(), is( batch.name() ) );
		assertThat( wireBatch.userNumber(), is( batch.userNumber() ) );
		assertThat( batch.generation(), is( wireBatch.generation() ) );
		assertThat( batch.section(), is( wireBatch.section() ) );
		assertThat( batch.sequence(), is( wireBatch.sequence() ) );
		assertThat( batch.set(), is( wireBatch.set() ) );
		assertThat( batch.version(), is( wireBatch.version() ) );
	}

	@Test
	public final void testFromWireNoPortfolio()
	{
		wireBatch = wireBuilder.id( BATCH_ID )
				.index( BATCH_IDX )
				.name( BATCH_NAME )
				.set( SET )
				.generation( GENERATION )
				.section( SECTION )
				.sequence( SEQUENCE )
				.version( VERSION )
				.build();

		final Batch batch = wireService.fromWire( wireBatch );

		assertThat( batch.id(), is( wireBatch.id() ) );
		assertThat( batch.portfolio()
				.isPresent(), is( false ) );
		assertThat( wireBatch.index(), is( batch.index() ) );
		assertThat( wireBatch.name(), is( batch.name() ) );
		assertThat( wireBatch.userNumber(), is( batch.userNumber() ) );
		assertThat( batch.generation(), is( wireBatch.generation() ) );
		assertThat( batch.section(), is( wireBatch.section() ) );
		assertThat( batch.sequence(), is( wireBatch.sequence() ) );
		assertThat( batch.set(), is( wireBatch.set() ) );
		assertThat( batch.version(), is( wireBatch.version() ) );
	}

}

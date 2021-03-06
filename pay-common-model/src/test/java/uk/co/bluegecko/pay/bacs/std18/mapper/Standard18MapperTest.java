package uk.co.bluegecko.pay.bacs.std18.mapper;


import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.function.BiConsumer;

import org.apache.commons.lang3.StringUtils;
import org.beanio.UnidentifiedRecordException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import uk.co.bluegecko.pay.bacs.std18.model.Contra;
import uk.co.bluegecko.pay.bacs.std18.model.Header1;
import uk.co.bluegecko.pay.bacs.std18.model.Header2;
import uk.co.bluegecko.pay.bacs.std18.model.Instruction;
import uk.co.bluegecko.pay.bacs.std18.model.Row;
import uk.co.bluegecko.pay.bacs.std18.model.UserHeader;
import uk.co.bluegecko.pay.bacs.std18.model.UserTrailer;
import uk.co.bluegecko.pay.bacs.std18.model.Volume;
import uk.co.bluegecko.pay.common.service.ParsingService;
import uk.co.bluegecko.pay.common.service.base.ParsingServiceBase;


public class Standard18MapperTest
{

	private static final String EMPTY = "";
	private static final String FORMAT = "F";
	private static final String AUDIT = "AUD0000";
	private static final String FILE_ID = "001";
	private static final String WORK_CODE = "4 MULTI";
	private static final String DEST = "999999";
	private static final String RECORD = "00106";
	private static final String OFFSET = "00";
	private static final String CCY = "AA";
	private static final String COUNTRY = "0000BB";
	private static final String BLOCK = "00512";
	private static final String LABEL = "1";
	private static final int SECTION = 1;
	private static final int SEQUENCE = 1;
	private static final int GENERATION = 819;
	private static final int VERSION = 4;
	private static final String FILE = "A100101S  1100101";
	private static final String SUN = "100101";
	private static final String SERIAL_NO = "173922";
	private static final String SORT_CODE = "402024";
	private static final String ACCT_NO = "21315692";
	private static final String ACCT_NAME = "BSDSAF 00000000055";
	private static final String ACCT_TYPE = "0";
	private static final String DEST_SORT_CODE = "010039";
	private static final String DEST_ACCT_NO = "01059963";
	private static final String DEST_ACCT_NAME = "OA NAME 09";
	private static final String CREDIT_CODE = "99";
	private static final String DEBIT_CODE = "17";
	private static final String NARRATIVE = "OSTEXT 09";
	private static final String RTI = "/000";
	private static final String REFERENCE = "REF&LT 00000000055";
	private static final int CREDIT_COUNT = 1;
	private static final int DEBIT_COUNT = 1;
	private static final int DDI_COUNT = 0;
	private static final BigDecimal VALUE = new BigDecimal( "0.55" );

	private static final String[] LINES =
		{ "VOL1173922                               100101                                1",
				"HDR1A100101S  11001011739220001000108194 08192 000000                           ",
				"HDR2F0051200106                                   00                            ",
				"UHL1 14308999999    AA0000BB4 MULTI  001       AUD0000                          ",
				"0100390105996309940202421315692/00000000000055BSDSAF 00000000055REF&LT 00000000055NAME   00000000055 14308",
				"4020242131569201740202421315692/00000000000055OSTEXT 09         CONTRA            OA NAME 09         16116",
				"EOF1A100101S  11001011739220001000108194 08192 000000                           ",
				"EOF2F0051200106                                   00                            ",
				"UTL10000000000055000000000005500000010000001        0000000                     " };

	private Standard18Mapper standard18Mapper;
	private BiConsumer< Row, Object > consumer;

	private ParsingService parsingService;

	@Before
	public void setUp() throws Exception
	{
		standard18Mapper = new Standard18Mapper();

		parsingService = new ParsingServiceBase();

		consumer = mock( BiConsumer.class );
	}

	@Test
	public final void testParseVol1() throws IOException
	{
		final Volume value = parseAndVerify( Row.VOL1, Volume.class );

		assertThat( value.serialNo(), is( SERIAL_NO ) );
		assertThat( value.accessibility(), is( EMPTY ) );
		assertThat( value.userNumber(), is( SUN ) );
		assertThat( value.label(), is( LABEL ) );
	}

	@Test
	public final void testParseHeader1() throws IOException
	{
		final Header1 value = parseAndVerify( Row.HDR1, Header1.class );

		assertThat( value.indicator(), is( Row.HDR1 ) );
		assertThat( value.file(), is( FILE ) );
		assertThat( value.set(), is( SERIAL_NO ) );
		assertThat( value.section(), is( SECTION ) );
		assertThat( value.sequence(), is( SEQUENCE ) );
		assertThat( value.generation(), is( GENERATION ) );
		assertThat( value.version(), is( VERSION ) );
		assertThat( value.created(), is( LocalDate.of( 1992, Month.JUNE, 6 ) ) );
		assertThat( value.expires(), is( LocalDate.of( 1970, Month.JANUARY, 1 ) ) );
		assertThat( value.accessibility(), is( EMPTY ) );
		assertThat( value.blockCount(), is( EMPTY ) );
		assertThat( value.systemCode(), is( EMPTY ) );
	}

	@Test
	public final void testParseHeader2() throws IOException
	{
		final Header2 value = parseAndVerify( Row.HDR2, Header2.class );

		assertThat( value.indicator(), is( Row.HDR2 ) );
		assertThat( value.format(), is( FORMAT ) );
		assertThat( value.block(), is( BLOCK ) );
		assertThat( value.offset(), is( OFFSET ) );
		assertThat( value.record(), is( RECORD ) );
	}

	@Test
	public final void testParseUserHeader() throws IOException
	{
		final UserHeader value = parseAndVerify( Row.UHL1, UserHeader.class );

		assertThat( value.processingDate(), is( LocalDate.of( 2009, Month.MARCH, 5 ) ) );
		assertThat( value.dest(), is( DEST ) );
		assertThat( value.currency(), is( CCY ) );
		assertThat( value.country(), is( COUNTRY ) );
		assertThat( value.workCode(), is( WORK_CODE ) );
		assertThat( value.file(), is( FILE_ID ) );
		assertThat( value.audit(), is( AUDIT ) );
	}

	@Test
	public final void testParseInstruction() throws IOException
	{
		final Instruction value = parseAndVerify( Row.INSTR, Instruction.class );

		assertThat( value.index(), is( 1 ) );
		assertThat( value.lineNo(), is( 1 ) );
		assertThat( value.origin()
				.sortCode(), is( SORT_CODE ) );
		assertThat( value.origin()
				.number(), is( ACCT_NO ) );
		assertThat( value.origin()
				.name(), is( ACCT_NAME ) );
		assertThat( value.destination()
				.sortCode(), is( DEST_SORT_CODE ) );
		assertThat( value.destination()
				.number(), is( DEST_ACCT_NO ) );
		assertThat( value.destination()
				.type(), is( ACCT_TYPE ) );
		assertThat( value.reference(), is( REFERENCE ) );
		assertThat( value.transactionType(), is( CREDIT_CODE ) );
		assertThat( value.rti(), is( RTI ) );
		assertThat( value.amount(), is( VALUE ) );
		assertThat( value.processingDate(), is( LocalDate.of( 2009, Month.MARCH, 5 ) ) );
	}

	@Test
	public final void testParseContra() throws IOException
	{
		final Contra value = parseAndVerify( Row.CONTRA, Contra.class );

		assertThat( value.index(), is( 1 ) );
		assertThat( value.lineNo(), is( 1 ) );
		assertThat( value.destination()
				.sortCode(), is( SORT_CODE ) );
		assertThat( value.destination()
				.number(), is( ACCT_NO ) );
		assertThat( value.destination()
				.type(), is( ACCT_TYPE ) );
		assertThat( value.transactionType(), is( DEBIT_CODE ) );
		assertThat( value.origin()
				.sortCode(), is( SORT_CODE ) );
		assertThat( value.origin()
				.number(), is( ACCT_NO ) );
		assertThat( value.origin()
				.name(), is( DEST_ACCT_NAME ) );
		assertThat( value.amount(), is( VALUE ) );
		assertThat( value.freeFormat(), is( RTI ) );
		assertThat( value.narrative(), is( NARRATIVE ) );
		assertThat( value.processingDate(), is( LocalDate.of( 2014, Month.FEBRUARY, 15 ) ) );
	}

	@Test
	public final void testParseEndOfFile1() throws IOException
	{
		final Header1 value = parseAndVerify( Row.EOF1, Header1.class );

		assertThat( value.indicator(), is( Row.EOF1 ) );
		assertThat( value.file(), is( FILE ) );
		assertThat( value.set(), is( SERIAL_NO ) );
		assertThat( value.section(), is( SECTION ) );
		assertThat( value.sequence(), is( SEQUENCE ) );
		assertThat( value.generation(), is( GENERATION ) );
		assertThat( value.version(), is( VERSION ) );
		assertThat( value.created(), is( LocalDate.of( 1992, Month.JUNE, 6 ) ) );
		assertThat( value.expires(), is( LocalDate.of( 1970, Month.JANUARY, 1 ) ) );
		assertThat( value.accessibility(), is( EMPTY ) );
		assertThat( value.blockCount(), is( EMPTY ) );
		assertThat( value.systemCode(), is( EMPTY ) );
	}

	@Test
	public final void testParseEndOfFile2() throws IOException
	{
		final Header2 value = parseAndVerify( Row.EOF2, Header2.class );

		assertThat( value.indicator(), is( Row.EOF2 ) );
		assertThat( value.format(), is( FORMAT ) );
		assertThat( value.block(), is( BLOCK ) );
		assertThat( value.offset(), is( OFFSET ) );
		assertThat( value.record(), is( RECORD ) );
	}

	@Test
	public final void testParseUserTrailer() throws IOException
	{
		final UserTrailer value = parseAndVerify( Row.UTL1, UserTrailer.class );

		assertThat( value.creditCount(), is( CREDIT_COUNT ) );
		assertThat( value.creditValue(), is( VALUE ) );
		assertThat( value.debitCount(), is( DEBIT_COUNT ) );
		assertThat( value.debitValue(), is( VALUE ) );
		assertThat( value.ddiCount(), is( DDI_COUNT ) );
		assertThat( value.serviceUser(), is( EMPTY ) );
	}

	@Test
	public final void testParseNoConsumer() throws IOException
	{
		standard18Mapper.addRow( Row.VOL1, consumer );

		parse( reader( LINES[Row.INSTR.ordinal()] ) );

		verify( consumer, never() ).accept( any(), any() );
	}

	@Test
	public final void testParseInvalidField() throws IOException
	{
		standard18Mapper.addRow( Row.HDR1, consumer );

		parse( reader( "HDR1A100101S  1100101173922         08194       000000                          " ) );

		verify( consumer, never() ).accept( any(), any() );
	}

	@Test
	public final void testParseInvalidRecord() throws IOException
	{
		standard18Mapper.addRow( Row.HDR1, consumer );

		parse( reader( "HDR1A100101S  11001011739220001000108194 08192 000000" ) );

		verify( consumer, never() ).accept( any(), any() );
	}

	@Test( expected = UnidentifiedRecordException.class )
	public final void testParseUnidentifiedRecord() throws IOException
	{
		standard18Mapper.addRow( Row.HDR1, consumer );

		parse( reader( "XXX1A100101S  11001011739220001000108194 08192 000000                          " ) );

		verify( consumer, never() ).accept( any(), any() );
	}

	@Test
	public final void testParseFileVerifyNumbering() throws IOException
	{
		standard18Mapper.addRow( Row.INSTR, consumer );
		standard18Mapper.addRow( Row.CONTRA, consumer );

		parse( reader( LINES ) );

		final ArgumentCaptor< Instruction > argInstruction = ArgumentCaptor.forClass( Instruction.class );
		final ArgumentCaptor< Contra > argContra = ArgumentCaptor.forClass( Contra.class );
		verify( consumer ).accept( eq( Row.INSTR ), argInstruction.capture() );
		verify( consumer ).accept( eq( Row.CONTRA ), argContra.capture() );

		final Instruction instruction = argInstruction.getValue();
		assertThat( instruction.index(), is( 1 ) );
		assertThat( instruction.lineNo(), is( 5 ) );

		final Contra contra = argContra.getValue();
		assertThat( contra.index(), is( 2 ) );
		assertThat( contra.lineNo(), is( 6 ) );
	}

	protected < T > T parseAndVerify( final Row row, final Class< T > type ) throws IOException
	{
		standard18Mapper.addRow( row, consumer );

		parse( reader( LINES[row.ordinal()] ) );

		final ArgumentCaptor< T > argument = ArgumentCaptor.forClass( type );
		verify( consumer ).accept( eq( row ), argument.capture() );
		return argument.getValue();
	}

	protected void parse( final Reader dataFile ) throws IOException
	{
		parsingService.parse( dataFile, standard18Mapper );
	}

	protected Reader reader( final String... lines )
	{
		return new StringReader( StringUtils.join( lines, '\n' ) );
	}

}

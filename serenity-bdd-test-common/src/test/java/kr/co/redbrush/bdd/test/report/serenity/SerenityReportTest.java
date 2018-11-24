package kr.co.redbrush.bdd.test.report.serenity;

import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.core.reports.AndContent;
import net.serenitybdd.core.reports.WithTitle;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@Slf4j
@RunWith(PowerMockRunner.class)
@PrepareForTest(Serenity.class)
public class SerenityReportTest {

    @InjectMocks
    private SerenityReport serenityReport = new SerenityReport();

    @Mock
    private JSONObject json;

    @Mock
    private WithTitle withTitle;

    @Mock
    private AndContent andContent;

    private String title = "title";
    private String contents = "contents";
    private String jsonBody = "{}";

    @Before
    public void before() {
        when(json.toString()).thenReturn(jsonBody);

        mockStatic(Serenity.class);

        when(Serenity.recordReportData()).thenReturn(withTitle);
        when(withTitle.withTitle(any(String.class))).thenReturn(andContent);
    }

    @Test
    public void testLogString() {
        serenityReport.log(title, contents);

        verify(withTitle).withTitle(title);
        verify(andContent).andContents(contents);
    }

    @Test
    public void testLogJson() {
        serenityReport.log(title, json);

        verify(withTitle).withTitle(title);
        verify(andContent).andContents(jsonBody);
    }
}

package kr.co.redbrush.bdd.test.report.serenity;


import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.core.Serenity;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SerenityReport {
    public void log(String title, String contents) {
        Serenity.recordReportData()
                .withTitle(title)
                .andContents(contents);
    }

    public void log(String title, JSONObject json) {
        Serenity.recordReportData()
                .withTitle(title)
                .andContents(json.toString());
    }
}

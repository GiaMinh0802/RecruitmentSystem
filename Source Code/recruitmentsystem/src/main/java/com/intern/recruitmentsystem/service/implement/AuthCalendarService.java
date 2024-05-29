package com.fpt.recruitmentsystem.service.implement;

import com.fpt.recruitmentsystem.exception.BadRequestException;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class AuthCalendarService {
    private static final Log logger = LogFactory.getLog(AuthCalendarService.class);
    private static final String APPLICATION_NAME = "";
    private HttpTransport httpTransport;
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private com.google.api.services.calendar.Calendar client;
    private static final String HCM_TIME_ZONE = "Asia/Ho_Chi_Minh";

    GoogleClientSecrets clientSecrets;
    GoogleAuthorizationCodeFlow flow;
    Credential credential;

    @Value("${google.client.client-id}")
    private String clientId;
    @Value("${google.client.client-secret}")
    private String clientSecret;
    @Value("${google.client.redirectUri}")
    private String redirectURI;

    public Event createEvent (String code, List<String> emailList, LocalDateTime localDateTime){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = localDateTime.format(formatter);
        LocalDateTime startDate = LocalDateTime.parse(formattedDateTime, formatter);
        LocalDateTime endDate = startDate.plusHours(1);

        Event event = new Event()
                .setSummary("New Interview")
                .setLocation("Fpt Software")
                .setDescription("Chi tiết cuộc phỏng vấn");
        try {
            TokenResponse response = flow.newTokenRequest(code).setRedirectUri(redirectURI).execute();
            credential = flow.createAndStoreCredential(response, "userID");
            client = new com.google.api.services.calendar.Calendar.Builder(httpTransport, JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME).build();

            ZoneId zoneId = ZoneId.of(HCM_TIME_ZONE);
            DateTime startDateTime = new DateTime(startDate.atZone(zoneId).toInstant().toEpochMilli());
            EventDateTime start = new EventDateTime()
                    .setDateTime(startDateTime)
                    .setTimeZone(HCM_TIME_ZONE);
            event.setStart(start);

            DateTime endDateTime = new DateTime(endDate.atZone(zoneId).toInstant().toEpochMilli());
            EventDateTime end = new EventDateTime()
                    .setDateTime(endDateTime)
                    .setTimeZone(HCM_TIME_ZONE);
            event.setEnd(end);

            ConferenceSolutionKey conferenceSolution = new ConferenceSolutionKey()
                    .setType("hangoutsMeet");
            CreateConferenceRequest createConferenceRequest = new CreateConferenceRequest()
                    .setRequestId("random-string-123")
                    .setConferenceSolutionKey(conferenceSolution);
            ConferenceData conferenceData = new ConferenceData()
                    .setCreateRequest(createConferenceRequest);
            event.setConferenceData(conferenceData);

            String[] recurrence = new String[] {"RRULE:FREQ=DAILY;COUNT=1"};
            event.setRecurrence(Arrays.asList(recurrence));

            EventAttendee[] attendees = new EventAttendee[emailList.size()];
            for (int i = 0; i < emailList.size(); i++) {
                attendees[i] = new EventAttendee().setEmail(emailList.get(i));
            }
            event.setAttendees(Arrays.asList(attendees));

            EventReminder[] reminderOverrides = new EventReminder[] {
                    new EventReminder().setMethod("email").setMinutes(24 * 60),
                    new EventReminder().setMethod("popup").setMinutes(10),
            };
            Event.Reminders reminders = new Event.Reminders()
                    .setUseDefault(false)
                    .setOverrides(Arrays.asList(reminderOverrides));
            event.setReminders(reminders);

            String calendarId = "primary";

            event = client.events().insert(calendarId, event).setConferenceDataVersion(1).execute();
        } catch (Exception e) {
            logger.warn("Exception while handling OAuth2 callback (" + e.getMessage() + ")."+ "\n"
                    + " Redirecting to google connection status page.");
        }
        return event;
    }

    public String authorize() throws Exception {
        AuthorizationCodeRequestUrl authorizationUrl;
        try{
            if (flow == null) {
                GoogleClientSecrets.Details web = new GoogleClientSecrets.Details();
                web.setClientId(clientId);
                web.setClientSecret(clientSecret);
                clientSecrets = new GoogleClientSecrets().setWeb(web);
                httpTransport = GoogleNetHttpTransport.newTrustedTransport();
                flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets,
                        Collections.singleton(CalendarScopes.CALENDAR)).build();
            }
            authorizationUrl = flow.newAuthorizationUrl().setRedirectUri(redirectURI);
            return authorizationUrl.build();
        }catch(Exception e){
          throw new BadRequestException("Cannot get link url " + e);
        }

    }
}

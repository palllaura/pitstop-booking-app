package com.smit.pitstop.views.bookings;

import com.smit.pitstop.service.PitstopService;
import com.smit.pitstop.service.model.ContactInformation;
import com.smit.pitstop.service.model.Location;
import com.smit.pitstop.service.model.TimeSlot;
import com.smit.pitstop.views.NotificationHelper;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BookTimeDialog extends Dialog {
    private static final Logger logger = LoggerFactory.getLogger(BookTimeDialog.class);

    private final Location chosenLocation;
    private final TimeSlot chosenTimeSlot;
    private final PitstopService service;
    private final NotificationHelper notificationHelper;
    private final Button submitButton;
    private final Button cancelButton;
    private TextField firstNameField;
    private TextField lastNameField;
    private EmailField email;
    private TextField phoneNumber;
    private TextArea comment;

    public BookTimeDialog(TimeSlot timeSlot,
                          Location location,
                          PitstopService pitstopService,
                          NotificationHelper helper
    ) {
        chosenTimeSlot = timeSlot;
        chosenLocation = location;
        service = pitstopService;
        notificationHelper = helper;

        this.setHeaderTitle("Book time for service");

        VerticalLayout dialogLayout = createDialogLayout();
        this.add(dialogLayout);

        submitButton = createSubmitButton();
        cancelButton = new Button("Cancel", e -> this.close());
        this.getFooter().add(cancelButton);
        this.getFooter().add(submitButton);
    }

    private VerticalLayout createDialogLayout() {
        H2 timeslot = new H2(chosenTimeSlot.toString());

        firstNameField = createInputField("First name");
        lastNameField = createInputField("Last name");

        email = new EmailField("Email");
        email.setRequired(true);
        email.setErrorMessage("Enter a valid email address");
        email.addValueChangeListener(e -> submitButton.setEnabled(validateInputFields()));

        phoneNumber = createInputField("Phone number");
        phoneNumber.setAllowedCharPattern("^\\+?[0-9]+$");
        phoneNumber.setErrorMessage("Enter a valid phone number");

        comment = new TextArea("Comment");
        comment.setMaxLength(50);
        comment.setAllowedCharPattern("[A-Za-z0-9,.\\-\\s]");
        comment.addValueChangeListener(e -> {
            e.getSource().setHelperText(e.getValue().length() + "/" + 50);
        });

        VerticalLayout dialogLayout = new VerticalLayout(timeslot, firstNameField,
                lastNameField, email, phoneNumber, comment);
        dialogLayout.setPadding(false);
        dialogLayout.setSpacing(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "18rem").set("max-width", "100%");

        return dialogLayout;
    }

    private TextField createInputField(String label) {
        TextField inputField = new TextField(label);
        inputField.setRequired(true);
        inputField.setAllowedCharPattern("[a-zA-Z\\s-]+");
        inputField.setErrorMessage("Field is required");
        inputField.addValueChangeListener(e -> submitButton.setEnabled(validateInputFields()));
        return inputField;
    }

    private Button createSubmitButton() {
        Button submitButton = new Button("Submit request");
        submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        submitButton.addClickListener(e -> submitRequest());
        submitButton.setEnabled(false);
        submitButton.setDisableOnClick(true);

        return submitButton;
    }

    private boolean validateInputFields() {
        return !firstNameField.getValue().isBlank() && firstNameField.getValue().matches("[a-zA-Z\\s-]+")
                && !lastNameField.getValue().isBlank() && lastNameField.getValue().matches("[a-zA-Z\\s-]+")
                && !email.getValue().isBlank() && email.getValue().matches("^[\\w-\\.]+@[\\w-]+\\.[a-z]{2,}$")
                && !phoneNumber.getValue().isBlank() && phoneNumber.getValue().matches("^\\+?[0-9]+$");
    }

    private void submitRequest() {
        ContactInformation info = createContactInformation();
        logger.info("Booking request submitted: " + info);

        boolean isSuccessful = service.sendBookingRequest(chosenLocation, chosenTimeSlot, info.toString());
        if (isSuccessful) {
            this.close();
            this.createSuccessDialog(info);
        } else {
            notificationHelper.createNotification(
                    "Unable to send booking request.",
                    "Please refresh the page and try again.",
                    NotificationVariant.LUMO_ERROR);
        }
    }

    private ContactInformation createContactInformation() {
        return new ContactInformation(
                firstNameField.getValue(),
                lastNameField.getValue(),
                email.getValue(),
                phoneNumber.getValue(),
                comment.getValue()
        );
    }

    private void createSuccessDialog(ContactInformation info) {
        Dialog successDialog = new Dialog();
        successDialog.setHeaderTitle("Booking request successful!");

        TextField location = createReadOnlyTextField("Location:", chosenLocation.getAddress());
        TextField time = createReadOnlyTextField("Time:", chosenTimeSlot.toString());
        TextField name = createReadOnlyTextField("Name:", info.firstName() + " " + info.lastName());
        TextField email = createReadOnlyTextField("Email:", info.email());
        TextField phone = createReadOnlyTextField("Phone:", info.phoneNumber());

        TextArea comment = new TextArea("Comment:");
        comment.setValue(info.comment());
        comment.setWidthFull();
        comment.setReadOnly(true);

        VerticalLayout infoLayout = new VerticalLayout(location, time, name, email, phone, comment);
        infoLayout.setSpacing(false);
        infoLayout.setPadding(false);
        successDialog.add(infoLayout);

        Button closeButton = new Button("Close", e -> successDialog.close());
        closeButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        successDialog.getFooter().add(closeButton);

        successDialog.open();
    }

    private TextField createReadOnlyTextField(String label, String value) {
        TextField textField = new TextField(label);
        textField.setValue(value);
        textField.setWidthFull();
        textField.setReadOnly(true);
        return textField;
    }
}

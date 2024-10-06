package com.smit.pitstop.views;

import com.vaadin.flow.component.HtmlComponent;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class NotificationHelper {

    public void createNotification(String line1, String line2, NotificationVariant variant) {
        Notification notification = new Notification();
        notification.addThemeVariants(variant);

        Div text = new Div(new Text(line1),
                new HtmlComponent("br"),
                new Text(line2));

        Button closeButton = createCloseButton();
        closeButton.addClickListener(event -> {
            notification.close();
        });

        HorizontalLayout layout = new HorizontalLayout(text, closeButton);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        notification.setPosition(Notification.Position.MIDDLE);

        notification.add(layout);
        notification.open();
    }

    private Button createCloseButton() {
        Button closeButton = new Button(new Icon("lumo", "cross"));
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        closeButton.setAriaLabel("Close");
        return closeButton;
    }
}

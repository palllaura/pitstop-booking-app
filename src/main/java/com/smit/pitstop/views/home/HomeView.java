package com.smit.pitstop.views.home;

import com.smit.pitstop.views.MainLayout;
import com.smit.pitstop.views.bookings.BookServiceView;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

@PageTitle("Home")
@Route(value = "", layout = MainLayout.class)
public class HomeView extends VerticalLayout {

    public HomeView() {
        RouterLink bookTime = new RouterLink(BookServiceView.class);
        bookTime.add(new Image("images/pitstop.png", "company logo"));
        add(bookTime);

        setSizeFull();
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }
}

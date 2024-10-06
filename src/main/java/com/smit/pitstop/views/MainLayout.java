package com.smit.pitstop.views;

import com.smit.pitstop.views.bookings.BookServiceView;
import com.smit.pitstop.views.home.HomeView;
import com.smit.pitstop.views.locations.LocationsView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class MainLayout extends AppLayout {

    private H1 viewTitle;

    public MainLayout() {
        setPrimarySection(Section.NAVBAR);
        addHeaderContent();
    }

    private void addHeaderContent() {
        HorizontalLayout navbar = createNavigation();

        viewTitle = new H1();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        addToNavbar(true, navbar);
    }


    private HorizontalLayout createNavigation() {
        HorizontalLayout navigation = new HorizontalLayout();
        navigation.addClassNames(LumoUtility.JustifyContent.CENTER,
                LumoUtility.Gap.SMALL, LumoUtility.Height.MEDIUM,
                LumoUtility.Width.FULL);
        navigation.add(createLink("HOME", HomeView.class),
                createLink("BOOK SERVICE", BookServiceView.class),
                createLink("LOCATIONS", LocationsView.class));
        return navigation;
    }

    private <T extends Component> RouterLink createLink(String viewName, Class<T> viewClass) {
        RouterLink link = new RouterLink(viewName, viewClass);
        link.addClassNames(LumoUtility.Display.FLEX,
                LumoUtility.AlignItems.CENTER,
                LumoUtility.Padding.Horizontal.MEDIUM,
                LumoUtility.TextColor.SECONDARY, LumoUtility.FontWeight.MEDIUM);
        link.getStyle().set("text-decoration", "none");

        return link;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}

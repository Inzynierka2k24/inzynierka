it("logs in", () => {
  cy.visit("localhost:4200");

  cy.intercept("http://localhost:8080/login", { body: "RandomToken" }).as(
    "login",
  );
  cy.intercept("http://localhost:8080/user/details", {
    fixture: "user",
  }).as("details");

  cy.get("#user-widget")
    .click()
    .then(() => {
      cy.get("#username").type("test");
      cy.get("#password").type("test24");
      cy.contains("Sign in").click();
    });
  cy.get("#user-widget").should("contain", "user");
});

describe("logged in as user", () => {
  beforeEach(() => {
    window.localStorage.setItem("JWT_TOKEN", "RandomToken");
    cy.intercept("http://localhost:8080/user/details", {
      fixture: "user",
    }).as("details");
    cy.visit("localhost:4200");
  });

  it("adds apartment listing", () => {
    cy.get("[data-pc-name='menubar']").within(() => {
      cy.contains("Apartments").click();
      cy.contains("Add apartment").click();
    });
    cy.url().should("include", "/apartments");

    cy.get("#dailyPrice").type("100");
    cy.get("#title").type("Test apartment");
    cy.get("#country").type("Poland");
    cy.get("#city").type("Warsaw");
    cy.get("#street").type("Test street");
    cy.get("#buildingNumber").type("1");
    cy.get("#apartmentNumber").type("1");
    cy.intercept("http://localhost:8080/1/apartment", { statusCode: 200 }).as(
      "addApartment",
    );
    cy.contains("Add apartment").click();
    cy.wait("@addApartment");
  });

  it("Adds and displays message order", () => {
    cy.intercept("http://localhost:8080/contacts/1", {
      fixture: "contactList",
    });
    cy.intercept("http://localhost:8080/1/apartment", {
      fixture: "apartmentList",
    });
    cy.get("[data-pc-name='menubar']").within(() => {
      cy.contains("Messages").click();
    });
    cy.get(".p-button-secondary").click();
    cy.get('[icon="pi pi-envelope"] > .p-ripple').click();
    cy.get("#apartments > .p-multiselect").click();
    cy.get(".p-multiselect-items").children().first().click();
    cy.get(".p-multiselect-header > .p-ripple").click();
    cy.get("#trigger > .p-dropdown").click();
    cy.get(".p-dropdown-items").children().first().click();
    cy.get("#intervalValue").clear().type("1");
    cy.get("#intervalType").click();
    cy.get(".p-dropdown-items").children().first().click();
    cy.get(".p-inputtextarea").type("Test message");
    cy.intercept("http://localhost:8080/messaging/1/contact/4", {
      fixture: "addMessage",
    }).as("addMessage");
    cy.get(".p-dialog-footer > :nth-child(2)").click();
    cy.wait("@addMessage");
    cy.get(".p-dialog-header-icons > .p-ripple").click();
    cy.wait(1000);
    cy.get(".p-button-secondary").first().click();
    cy.contains("Notifications").click();
    cy.get(".p-datatable-tbody > :nth-child(2) > :nth-child(3)").should(
      "contain",
      "Test message",
    );
  });

  it("should add external account", () => {
    cy.intercept("http://localhost:8080/1/external/account", {
      body: {
        login: "test",
        password: "test24",
        serviceType: "NOCOWANIEPL",
      },
    }).as("addExternalAccount");
    cy.get("#user-widget")
      .click()
      .then(() => {
        cy.contains("Settings").click();
      });
    cy.get(".pi-plus").click();
    cy.get("#service3 > .p-radiobutton > .p-radiobutton-box").click();
    cy.get("#login").type("test");
    cy.get("#password").type("test24");
    cy.get(".p-button-primary").click();
  });
});

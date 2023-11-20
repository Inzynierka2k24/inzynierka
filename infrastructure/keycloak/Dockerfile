FROM quay.io/keycloak/keycloak:latest AS builder

# Configure a database vendor
ENV KC_DB=postgres

WORKDIR /opt/keycloak
RUN /opt/keycloak/bin/kc.sh build

FROM quay.io/keycloak/keycloak:latest
COPY --from=builder /opt/keycloak/ /opt/keycloak/
COPY ./realm-export.json /opt/keycloak/data/import/
EXPOSE 8090

ENTRYPOINT ["/opt/keycloak/bin/kc.sh"]
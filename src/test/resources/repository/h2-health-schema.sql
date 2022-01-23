CREATE TABLE health_status
(
    id        INT AUTO_INCREMENT,
    app_id    VARCHAR2(64) NOT NULL PRIMARY KEY,
    url       VARCHAR2(64) NOT NULL,
    poll_time TIMESTAMP,
    registration_time TIMESTAMP,
    status    VARCHAR2(24)
);

COMMENT ON COLUMN health_status.id IS 'Identifier of the application';
COMMENT ON COLUMN health_status.app_id IS 'Application name';
COMMENT ON COLUMN health_status.url IS 'Application health check url';
COMMENT ON COLUMN health_status.poll_time IS 'Last health check request time';
COMMENT ON COLUMN health_status.status IS 'Status of application';
COMMENT ON COLUMN health_status.registration_time IS 'Time of registration of application';

INSERT INTO health_status (app_id, url, poll_time, status,registration_time)
VALUES ('appointment_scheduler', 'https://localhost:8081/appointment-scheduler/manage/health', {ts '2022-01-20 07:23:48.112'}, 'UP', {ts '2022-01-20 07:23:48.112'});

INSERT INTO health_status (app_id, url, poll_time, status,registration_time) VALUES ('customer_service', 'https://localhost:8082/customer-service/manage/health', {ts '2022-01-20 07:23:48.113'}, 'DOWN', {ts '2022-01-20 07:23:48.112'});

INSERT INTO health_status (app_id, url, poll_time, status,registration_time)VALUES ('doctor_service', 'https://localhost:8083/doctor-service/manage/health', {ts '2022-01-20 07:23:48.114'}, 'UP', {ts '2022-01-20 07:23:48.112'});
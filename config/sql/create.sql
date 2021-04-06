
-- USER_T
CREATE TABLE public.user_t (
	username varchar(255) NOT NULL,
	password varchar(255) NOT NULL,
	"name" varchar(255) NOT NULL,
	surname varchar(255) NOT NULL,
	email varchar(255) NOT NULL,
	status bpchar(1) NOT NULL,
	insert_time timestamp NOT NULL,
	update_time timestamp NULL,
	"version" numeric(19) NOT NULL,
	CONSTRAINT pk_user PRIMARY KEY (username)
);

-- PROFILE_T
CREATE TABLE public.role_t
(
    id numeric(19) NOT NULL,
    code varchar(255) NOT NULL,
    description varchar(255) NOT NULL,
    CONSTRAINT pk_profile PRIMARY KEY (id)
);

INSERT INTO public.role_t (id, code, description)
VALUES (1, 'ADMIN', 'Admin role, read/write');
INSERT INTO public.role_t (id, code, description)
VALUES (2, 'USER', 'User role, readonly');

-- PROFILE_FUNCTIONALITY_T
CREATE TABLE public.role_user_t
(
    role_id numeric(19) NOT NULL,
    user_id varchar(255) NOT NULL,
    CONSTRAINT pk_role_user PRIMARY KEY (role_id, user_id)
);
ALTER TABLE public.role_user_t ADD CONSTRAINT fk_role_user FOREIGN KEY (role_id) REFERENCES public.role_t(id);
ALTER TABLE public.role_user_t ADD CONSTRAINT fk_user_role FOREIGN KEY (user_id) REFERENCES public.user_t(username);
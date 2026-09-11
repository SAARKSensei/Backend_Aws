-- Auto-generated and formatted schema

CREATE TABLE IF NOT EXISTS campaign (
    end_date date,
    is_active boolean,
    start_date date,
    created_at timestamp(6),
    id uuid not null,
    description varchar(255),
    name varchar(255) not null,
    primary key (id)
);

CREATE TABLE IF NOT EXISTS child_digital_activity_progress (
    completed_at timestamp(6),
    started_at timestamp(6),
    child_id uuid not null,
    digital_activity_id uuid not null,
    id uuid not null,
    status varchar(255) not null,
    primary key (id),
    unique (child_id, digital_activity_id)
);

CREATE TABLE IF NOT EXISTS child_interactive_activity_progress (
    completed_at timestamp(6),
    started_at timestamp(6),
    child_id uuid not null,
    id uuid not null,
    interactive_activity_id uuid not null,
    status varchar(255) not null,
    primary key (id)
);

CREATE TABLE IF NOT EXISTS child_life_skill (
    score integer not null,
    created_at timestamp(6) not null,
    updated_at timestamp(6) not null,
    child_id uuid not null,
    id uuid not null,
    life_skill enum ('COPING_WITH_EMOTIONS','COPING_WITH_STRESS','CREATIVE_THINKING','CRITICAL_THINKING','DECISION_MAKING','EFFECTIVE_COMMUNICATION','EMPATHY','INTERPERSONAL_RELATIONSHIP','PROBLEM_SOLVING','SELF_AWARENESS') not null,
    primary key (id)
);

CREATE TABLE IF NOT EXISTS child_question_attempt (
    attempt_number integer,
    is_correct boolean,
    attempted_at timestamp(6),
    child_id uuid not null,
    id uuid not null,
    option_id uuid not null,
    question_id uuid not null,
    primary key (id)
);

CREATE TABLE IF NOT EXISTS child_submodule_completion (
    score numeric(38,2),
    completed_at timestamp(6),
    child_id uuid not null,
    id uuid not null,
    sub_module_id uuid not null,
    remarks varchar(255),
    primary key (id)
);

CREATE TABLE IF NOT EXISTS child_user (
    date_of_birth date,
    plan_expiry_date date,
    plan_start_date date,
    created_at timestamp(6) not null,
    updated_at timestamp(6) not null,
    active_plan_id uuid,
    child_id uuid not null,
    parent_id uuid not null,
    age_group varchar(255),
    blood_group varchar(255),
    child_name varchar(255),
    gender varchar(255),
    grade varchar(255),
    phone_number varchar(255),
    school_id varchar(255),
    plan_status enum ('ACTIVE','EXPIRED','NONE'),
    primary key (child_id)
);

CREATE TABLE IF NOT EXISTS coupon (
    discount_value integer not null,
    is_active boolean,
    max_discount integer,
    max_usage integer,
    min_order_amount integer,
    used_count integer,
    created_at timestamp(6),
    expiry_date timestamp(6),
    applicable_pricing_plan_id uuid,
    id uuid not null,
    code varchar(255) not null unique,
    discount_type varchar(255) not null,
    primary key (id)
);

CREATE TABLE IF NOT EXISTS coupon_usage (
    discount_amount integer not null,
    used_at timestamp(6),
    coupon_id uuid not null,
    id uuid not null,
    master_transaction_id uuid,
    parent_id uuid not null,
    pricing_plan_id uuid not null,
    primary key (id),
    unique (coupon_id, parent_id)
);

CREATE TABLE IF NOT EXISTS digital_activity (
    is_active boolean,
    order_index integer,
    created_at timestamp(6) not null,
    updated_at timestamp(6) not null,
    id uuid not null,
    sub_module_id uuid not null,
    difficulty text,
    game_type text,
    instructions text,
    title text not null,
    primary key (id)
);

CREATE TABLE IF NOT EXISTS interactive_activity (
    is_active boolean,
    order_index integer,
    created_at timestamp(6) not null,
    updated_at timestamp(6) not null,
    id uuid not null,
    sub_module_id uuid not null,
    activity_type text,
    cover_image text,
    key_objectives text,
    learning_outcome text,
    objective text,
    reference_video text,
    title text not null,
    primary key (id)
);

CREATE TABLE IF NOT EXISTS interactive_process (
    step_order integer not null,
    created_at timestamp(6) not null,
    updated_at timestamp(6) not null,
    id uuid not null,
    interactive_activity_id uuid not null,
    sub_module_id uuid,
    child_task text,
    hint text,
    media_url text,
    sensei_message text,
    primary key (id)
);

CREATE TABLE IF NOT EXISTS interactive_process_substep (
    step_order integer,
    completed_at timestamp(6),
    created_at timestamp(6),
    started_at timestamp(6),
    updated_at timestamp(6),
    id uuid not null,
    process_id uuid not null,
    status varchar(255),
    step_text text,
    primary key (id)
);

CREATE TABLE IF NOT EXISTS interactive_process_tracking (
    completed_at timestamp(6),
    created_at timestamp(6),
    started_at timestamp(6),
    updated_at timestamp(6),
    child_id uuid not null,
    id uuid not null,
    interactive_process_id uuid not null,
    status enum ('COMPLETED','IN_PROGRESS','NOT_STARTED') not null,
    primary key (id),
    unique (child_id, interactive_process_id)
);

CREATE TABLE IF NOT EXISTS master_transaction (
    amount integer,
    created_at timestamp(6),
    child_id uuid,
    id uuid not null,
    parent_id uuid,
    payment_transaction_id uuid,
    pricing_plan_id uuid,
    currency varchar(255),
    remarks varchar(255),
    transaction_status enum ('CANCELLED','DISPUTED','FAILED','PENDING','PROCESSING','REFUNDED','REVERSED','SUCCESS'),
    transaction_type enum ('CAMPAIGN_REWARD','CASHBACK','COMMISSION','PLAN_PURCHASE','PURCHASE','REFERRAL_REWARD','REFUND','SCHOLARSHIP','SUBSCRIPTION','WALLET_TOPUP'),
    primary key (id)
);

CREATE TABLE IF NOT EXISTS module (
    is_active boolean,
    module_order integer,
    order_index integer not null,
    created_at timestamp(6) not null,
    updated_at timestamp(6) not null,
    id uuid not null,
    subject_id uuid not null,
    description text,
    name text not null,
    primary key (id)
);

CREATE TABLE IF NOT EXISTS parent_quiz_attempt (
    completed_at timestamp(6) not null,
    child_id uuid not null,
    id uuid not null,
    parent_id uuid not null,
    primary key (id)
);

CREATE TABLE IF NOT EXISTS parent_quiz_option (
    created_at timestamp(6) not null,
    id uuid not null,
    question_id uuid not null,
    option_text text not null,
    primary key (id)
);

CREATE TABLE IF NOT EXISTS parent_quiz_option_life_skills (
    option_id uuid not null,
    life_skill enum ('COPING_WITH_EMOTIONS','COPING_WITH_STRESS','CREATIVE_THINKING','CRITICAL_THINKING','DECISION_MAKING','EFFECTIVE_COMMUNICATION','EMPATHY','INTERPERSONAL_RELATIONSHIP','PROBLEM_SOLVING','SELF_AWARENESS')
);

CREATE TABLE IF NOT EXISTS parent_quiz_question (
    is_active boolean not null,
    order_index integer,
    created_at timestamp(6) not null,
    id uuid not null,
    question_text text not null,
    primary key (id)
);

CREATE TABLE IF NOT EXISTS parent_user (
    date_of_birth date,
    spouse_date_of_birth date,
    created_at timestamp(6) not null,
    updated_at timestamp(6) not null,
    parent_id uuid not null,
    email varchar(255) unique,
    location varchar(255),
    marital_status varchar(255),
    name varchar(255),
    occupation varchar(255),
    password varchar(255),
    phone varchar(255),
    relation_with_children varchar(255),
    spouse_email varchar(255),
    spouse_gender varchar(255),
    spouse_name varchar(255),
    spouse_occupation varchar(255),
    spouse_phone varchar(255),
    spouse_relation_with_child varchar(255),
    user_name varchar(255) unique,
    primary key (parent_id)
);

CREATE TABLE IF NOT EXISTS payment_transaction (
    amount integer,
    coupon_discount integer,
    emi_months integer,
    is_emi boolean,
    created_at timestamp(6),
    updated_at timestamp(6),
    child_id uuid,
    id uuid not null,
    parent_id uuid,
    pricing_plan_id uuid,
    coupon_code varchar(255),
    currency varchar(255),
    failure_reason varchar(255),
    gateway_order_id varchar(255),
    gateway_payment_id varchar(255),
    gateway_signature varchar(255),
    raw_response text,
    gateway enum ('CASHFREE','GPAY','INTERNAL_WALLET','MANUAL','NONE','PAYTM','PAYU','PHONEPE','RAZORPAY','STRIPE'),
    payment_method enum ('BANK_TRANSFER','CARD','CASH','EMI','NETBANKING','OTHER','UPI','WALLET'),
    status enum ('CANCELLED','DISPUTED','FAILED','PENDING','PROCESSING','REFUNDED','REVERSED','SUCCESS'),
    transaction_type enum ('CAMPAIGN_REWARD','CASHBACK','COMMISSION','PLAN_PURCHASE','PURCHASE','REFERRAL_REWARD','REFUND','SCHOLARSHIP','SUBSCRIPTION','WALLET_TOPUP') not null,
    primary key (id)
);

CREATE TABLE IF NOT EXISTS pricing_plan (
    duration_in_months integer,
    price integer not null,
    created_at timestamp(6) not null,
    updated_at timestamp(6) not null,
    id uuid not null,
    description text,
    grade varchar(255),
    name varchar(255) not null,
    status varchar(255) not null,
    primary key (id)
);

CREATE TABLE IF NOT EXISTS pricing_plan_subject (
    created_at timestamp(6) not null,
    updated_at timestamp(6) not null,
    id uuid not null,
    pricing_plan_id uuid not null,
    subject_id uuid not null,
    primary key (id),
    unique (pricing_plan_id, subject_id)
);

CREATE TABLE IF NOT EXISTS question (
    is_active boolean,
    order_index integer,
    digital_activity_id uuid not null,
    id uuid not null,
    counsellor_note text,
    explanation text,
    hint text,
    question_text text not null,
    primary key (id)
);

CREATE TABLE IF NOT EXISTS question_option (
    is_active boolean,
    is_correct boolean,
    order_index integer,
    id uuid not null,
    question_id uuid not null,
    counsellor_note text,
    hint text,
    option_text text not null,
    status varchar(255),
    primary key (id)
);

CREATE TABLE IF NOT EXISTS referral_code (
    is_active boolean not null,
    max_usage integer not null,
    used_count integer not null,
    created_at timestamp(6),
    id uuid not null,
    parent_id uuid not null unique,
    code varchar(255) not null unique,
    primary key (id)
);

CREATE TABLE IF NOT EXISTS referral_usage (
    reward_amount integer not null,
    used_at timestamp(6) not null,
    id uuid not null,
    referral_code_id uuid not null,
    referred_parent_id uuid not null,
    referrer_parent_id uuid not null,
    primary key (id),
    unique (referral_code_id, referred_parent_id)
);

CREATE TABLE IF NOT EXISTS sub_module (
    is_active boolean,
    order_index integer not null,
    submodule_order integer,
    created_at timestamp(6) not null,
    updated_at timestamp(6) not null,
    id uuid not null,
    module_id uuid not null,
    description text,
    name text not null,
    primary key (id)
);

CREATE TABLE IF NOT EXISTS subject (
    is_active boolean not null,
    created_at timestamp(6) not null,
    updated_at timestamp(6) not null,
    id uuid not null,
    age_group text,
    description text,
    icon_url text,
    name text not null,
    primary key (id)
);

CREATE TABLE IF NOT EXISTS wallet (
    balance integer,
    created_at timestamp(6) not null,
    updated_at timestamp(6) not null,
    id uuid not null,
    parent_id uuid not null unique,
    status varchar(20),
    primary key (id)
);

CREATE TABLE IF NOT EXISTS wallet_transaction (
    amount integer not null,
    balance_after integer not null,
    created_at timestamp(6),
    id uuid not null,
    parent_id uuid not null,
    reference_id uuid,
    wallet_id uuid not null,
    direction varchar(255) not null,
    reference_type varchar(255),
    remarks varchar(255),
    transaction_type varchar(255) not null,
    primary key (id)
);

ALTER TABLE IF EXISTS child_digital_activity_progress ADD CONSTRAINT FKeckhxlm9tfh2h5095qe01mudf FOREIGN KEY (digital_activity_id) REFERENCES digital_activity;
ALTER TABLE IF EXISTS child_interactive_activity_progress ADD CONSTRAINT FKh2tt5p29o115pck1t0v332ebp FOREIGN KEY (interactive_activity_id) REFERENCES interactive_activity;
ALTER TABLE IF EXISTS child_life_skill ADD CONSTRAINT FKccxp2pfffmmf3mkyuyc4w670u FOREIGN KEY (child_id) REFERENCES child_user;
ALTER TABLE IF EXISTS child_question_attempt ADD CONSTRAINT FKn4s7149f552wrwr08kv44aua FOREIGN KEY (option_id) REFERENCES question_option;
ALTER TABLE IF EXISTS child_question_attempt ADD CONSTRAINT FKgusdj8wxtopfkj8epvn2m7tdy FOREIGN KEY (question_id) REFERENCES question;
ALTER TABLE IF EXISTS child_submodule_completion ADD CONSTRAINT FKk7q2gx3fi0ants4q3s600tygc FOREIGN KEY (sub_module_id) REFERENCES sub_module;
ALTER TABLE IF EXISTS child_user ADD CONSTRAINT FKbj5dhahdb2d1ufbif50vwyh69 FOREIGN KEY (parent_id) REFERENCES parent_user;
ALTER TABLE IF EXISTS digital_activity ADD CONSTRAINT FKgwp96s8nsd58jbxl79cqmjawq FOREIGN KEY (sub_module_id) REFERENCES sub_module;
ALTER TABLE IF EXISTS interactive_activity ADD CONSTRAINT FK8ihddxn6bms2g5qnpn4swgv5u FOREIGN KEY (sub_module_id) REFERENCES sub_module;
ALTER TABLE IF EXISTS interactive_process ADD CONSTRAINT FKcnld6yw85u646umf3b0u60mxn FOREIGN KEY (interactive_activity_id) REFERENCES interactive_activity;
ALTER TABLE IF EXISTS interactive_process ADD CONSTRAINT FKaql1efcp9ux7ouhk6csc3snih FOREIGN KEY (sub_module_id) REFERENCES sub_module;
ALTER TABLE IF EXISTS interactive_process_substep ADD CONSTRAINT FK1gdnuieiy3s181oo4ggxwj79 FOREIGN KEY (process_id) REFERENCES interactive_process;
ALTER TABLE IF EXISTS interactive_process_tracking ADD CONSTRAINT FK696jmqp4ph49qn2wqt3cc2ujr FOREIGN KEY (child_id) REFERENCES child_user;
ALTER TABLE IF EXISTS interactive_process_tracking ADD CONSTRAINT FKj0pmj1qtlscnukce1rjo2u8ot FOREIGN KEY (interactive_process_id) REFERENCES interactive_process;
ALTER TABLE IF EXISTS module ADD CONSTRAINT FK64fjy7xrf5c5uct709tukfaqj FOREIGN KEY (subject_id) REFERENCES subject;
ALTER TABLE IF EXISTS parent_quiz_attempt ADD CONSTRAINT FK1c1ig1egr8s7j2j842nep8g3r FOREIGN KEY (child_id) REFERENCES child_user;
ALTER TABLE IF EXISTS parent_quiz_attempt ADD CONSTRAINT FK6aan2lmao4cgxxdaoxa61yry8 FOREIGN KEY (parent_id) REFERENCES parent_user;
ALTER TABLE IF EXISTS parent_quiz_option ADD CONSTRAINT FKk8wg8syidea8b2p59lfis03nh FOREIGN KEY (question_id) REFERENCES parent_quiz_question;
ALTER TABLE IF EXISTS parent_quiz_option_life_skills ADD CONSTRAINT FK3hr8ws6dksawjw98bgmnwoqua FOREIGN KEY (option_id) REFERENCES parent_quiz_option;
ALTER TABLE IF EXISTS pricing_plan_subject ADD CONSTRAINT FKjt2btay5b0jei0uis66l421b9 FOREIGN KEY (pricing_plan_id) REFERENCES pricing_plan;
ALTER TABLE IF EXISTS pricing_plan_subject ADD CONSTRAINT FKpshvv8jtpvmp9hop11v0jrekx FOREIGN KEY (subject_id) REFERENCES subject;
ALTER TABLE IF EXISTS question ADD CONSTRAINT FKqo8btit89433v670xr46owgor FOREIGN KEY (digital_activity_id) REFERENCES digital_activity;
ALTER TABLE IF EXISTS question_option ADD CONSTRAINT FKmmdv54rmm5hkgxbn1008ix87n FOREIGN KEY (question_id) REFERENCES question;
ALTER TABLE IF EXISTS sub_module ADD CONSTRAINT FKsiy8j1vihx4qrh9ap5mdfkn13 FOREIGN KEY (module_id) REFERENCES module;

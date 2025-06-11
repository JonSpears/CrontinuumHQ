-- Enables PostgreSQL extension for UUIDs
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Adds tenant column to jobs table
ALTER TABLE jobs ADD COLUMN tenant_id UUID NOT NULL;

-- Creates RLS policy
CREATE POLICY tenant_isolation_policy ON jobs
    USING (tenant_id = current_setting('app.current_tenant')::UUID);

-- Enables RLS
ALTER TABLE jobs ENABLE ROW LEVEL SECURITY;
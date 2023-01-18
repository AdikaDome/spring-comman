# Copyright 2022 NXGN Management, LLC. All Rights Reserved.

# Declares local values including environment specific values

locals {
  application_suite     = "pxp"
  application_component = "spring-common"
  application_name      = "spring-common"
  ecr_name              = "${local.application_suite}-${local.application_component}"

  s3_artifacts_target_bucket_name = "nextgen-aws-pxp-mf-build-artifacts-us-east-2"

  git_branch_trigger_pipeline = "master"

  common_tags = {
    "nextgen.automation"          = "true"
    "nextgen.environment"         = "pipeline"
    "nextgen.environment-type"    = "build"
    "nextgen.data-classification" = "confidential"
    "nextgen.component"           = "PXP"
    "pxp.application"             = "Platform"
    "pxp.component"               = "spring-common"
  }

  valid_workspaces = [
    "default"
  ]
  is_workspace_valid = {
    true = null
  }
  test_workspace = local.is_workspace_valid[contains(local.valid_workspaces, terraform.workspace)] # An error on this line, usually indicates a blocked workspace
}
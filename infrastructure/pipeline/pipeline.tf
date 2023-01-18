# Copyright 2022 NXGN Management, LLC. All Rights Reserved.

# Specify configuration details for the CI/CD pipeline

module "pipeline" {
  source = "git::ssh://bitbucket.nextgen.com:7999/dope/codepipeline_framework.git//predefined_pipelines/bitbucket_codecommit_codebuild?ref=0.12.1"
  providers = {
    aws = aws
  }

  application_name            = local.application_name
  source_bitbucket_project    = "MFPLAT"
  source_bitbucket_repo       = "spring-common"
  git_branch_trigger_pipeline = local.git_branch_trigger_pipeline

  s3_artifact_bucket_name      = local.s3_artifacts_target_bucket_name
  codeartifact_repository_name = "pxp-mf"
  codeartifact_domain          = "nextgen-pxp-mf-build"

  codebuild_environment_variables = [
    {
      name  = "AWS_ACCOUNT_ID"
      value = data.aws_caller_identity.current.account_id
      type  = "PLAINTEXT"
    },
    {
      name  = "APPLICATION_NAME"
      value = local.application_name
      type  = "PLAINTEXT"
    },
    {
      name  = "BRANCH_NAME"
      value = local.git_branch_trigger_pipeline
      type  = "PLAINTEXT"
    },
    {
      name  = "REGION"
      value = data.aws_region.current.name
      type  = "PLAINTEXT"
    },
  ]
  codebuild_environment_privileged_mode = true

  common_tags = local.common_tags
}
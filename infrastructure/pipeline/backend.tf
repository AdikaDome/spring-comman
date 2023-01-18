# Copyright 2022 NXGN Management, LLC. All Rights Reserved.

# Remote State Configuration

terraform {
  ##DON'T CHANGE UNLESS YOU KNOW WHAT YOU'RE DOING
  backend "s3" {
    region         = "us-east-2"
    role_arn       = "arn:aws:iam::997401518295:role/NextGenAdminAccess"
    bucket         = "nextgen-aws-pxp-mf-build-terraform-state-us-east-2"
    key            = "spring-common/pipeline.tfstate"
    dynamodb_table = "terraform-state"
  }
}
# Copyright 2022 NXGN Management, LLC. All Rights Reserved.

# Specify configuration details for AWS terraform providers

provider "aws" {
  region = "us-east-2"
  assume_role {
    role_arn = "arn:aws:iam::997401518295:role/NextGenAdminAccess"
  }
}
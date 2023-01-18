# Copyright 2022 NXGN Management, LLC. All Rights Reserved.

# Specify the version constraints for terraform and providers

terraform {
  required_version = ">= 1.0"

  required_providers {
    aws = {
      version = "~> 3.0"
      source  = "hashicorp/aws"
    }
  }
}
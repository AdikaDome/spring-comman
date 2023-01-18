# Copyright 2022 NXGN Management, LLC. All Rights Reserved.

# Returns output values

output "pipeline_name" {
  description = "Name of the pipeline created."
  value       = module.pipeline.pipeline_name
}

output "aws_codecommit_repository" {
  description = "Object from terraform aws_codecommit_repository resource."
  value       = module.pipeline.aws_codecommit_repository
}

output "codecommit_repo_name" {
  description = "Name of CodeCommit repository created for the pipeline."
  value       = module.pipeline.codecommit_repo_name
}

output "codecommit_clone_url_http" {
  description = "Clone URL used when mirroring from BitBucket."
  value       = module.pipeline.codecommit_clone_url_http
}
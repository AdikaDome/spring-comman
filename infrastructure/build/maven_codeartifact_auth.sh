#!/bin/bash

# Copyright 2022 NXGN Management, LLC. All Rights Reserved.

# Generates auth token to authenticate to AWS CodeArtifact repository
# Add token to maven config

CODEARTIFACT_AUTH_TOKEN=`aws codeartifact get-authorization-token --domain nextgen-pxp-mf-build --domain-owner 997401518295 --query authorizationToken --output text`

sed -i '/<\/servers>/i \
        <server>\n \
        <id>nextgen-pxp-mf-build--pxp-mf</id>\n \
        <username>aws</username>\n \
        <password>${CODEARTIFACT_AUTH_TOKEN}</password>\n \
        </server>\n \
        ' /opt/maven/conf/settings.xml
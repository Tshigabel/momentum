# Read Me First
The following was discovered as part of building this project:

* The original package name 'com.momentum.investments.moment-form-generator-service' is invalid and this project uses 'com.momentum.investments.momentformgeneratorservice' instead.

# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

1. docker-compose -f docker-compose-services.yml up -d --build
2.  aws --endpoint-url=http://localhost:4566 s3 mb s3://raw-csv-form
3.  aws --endpoint-url=http://localhost:4566 s3 cp .\src\main\resources\csv_form\test.csv  s3://raw-csv-form
4. aws --endpoint-url=http://localhost:4566 s3 mb s3://pfd-form

aws --endpoint-url=http://localhost:4566 s3 mb s3://raw-csv-form
aws --endpoint-url=http://localhost:4566 s3 mb s3://pfd-form
aws --endpoint-url=http://localhost:4566 s3 cp .\src\main\resources\csv_form\test.csv  s3://raw-csv-form
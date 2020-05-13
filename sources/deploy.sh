#!/bin/bash

heroku deploy:jar backend/target/backend-latest.jar -i Procfile --app sigma17


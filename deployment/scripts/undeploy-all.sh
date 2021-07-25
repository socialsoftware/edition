#!/bin/bash

kubectl delete -f deployment/frontend/
kubectl delete -f deployment/backend/
kubectl delete -f deployment/mysql/

exit 0
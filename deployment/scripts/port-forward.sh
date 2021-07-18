#!/bin/bash
kubectl apply -f deployment/docker-front-end.yaml
sleep 15
kubectl port-forward svc/docker-service 8080:8080
exit 0
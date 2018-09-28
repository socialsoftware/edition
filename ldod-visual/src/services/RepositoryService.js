import axios from 'axios';

export class RepositoryService {
    constructor() {
        this.axios = axios.create({
            baseURL: 'http://localhost:8080',
            timeout: 1000,
            headers: {
                'X-Custom-Header': 'BlendedWorkflow'
            }
        });
    }

    // Specifications
    getFragments(acronym) {
        return this.axios.get("/virtualeditions/acronym/"+acronym+"/fragments");
    }

    getTranscriptions(acronym) {
        return this.axios.get("/virtualeditions/acronym/"+acronym+"/transcriptions");
    }

}

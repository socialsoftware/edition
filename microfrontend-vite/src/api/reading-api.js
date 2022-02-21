
import fetcher from './axios';


export const getReadingExperts = async () => await fetcher.get('/microfrontend/reading');
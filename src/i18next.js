import i18next from 'i18next';
import pt from './translations/pt.json';
import en from './translations/en.json';
import es from './translations/es.json';

i18next
    .init({
        interpolation: {
            // React already does escaping
            escapeValue: false,
        },
        lng: 'pt', // 'en' | 'es'
        // Using simple hardcoded resources for simple example
        resources: {
            pt,
            en,
            es,
        },
    });

export default i18next;

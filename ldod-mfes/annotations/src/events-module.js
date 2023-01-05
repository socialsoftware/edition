import { ldodEventPublisher } from '@shared/ldod-events.js';

export const errorPublisher = error => ldodEventPublisher('error', error);

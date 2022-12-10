import { ldodEventPublisher } from 'shared/ldod-events.js';

export const loadingPublisher = (isLoading) =>
  ldodEventPublisher('loading', isLoading);

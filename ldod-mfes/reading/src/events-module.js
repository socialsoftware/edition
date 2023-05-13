/** @format */

import { ldodEventPublisher } from '@core';

export const errorPublisher = error => ldodEventPublisher('error', error);

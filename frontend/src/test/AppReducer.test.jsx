import { describe, expect, it } from 'vitest';
import { appReducer, initialAppState } from '../context/app/AppReducer';
import { BOOTSTRAP_SUCCESS } from '../context/lists/ListsActions';

describe('appReducer', () => {
    it('maps the bootstrap sales link to salesHref', () => {
        const action = {
            type: BOOTSTRAP_SUCCESS,
            payload: {
                sales: { href: 'http://localhost:8081/sales' },
            },
        };

        const state = appReducer(initialAppState, action);

        expect(state.salesHref).toBe('http://localhost:8081/sales');
    });

    it('sets salesHref to null when bootstrap has no sales link', () => {
        const currentState = {
            ...initialAppState,
            salesHref: 'http://localhost:8081/sales',
        };
        const action = {
            type: BOOTSTRAP_SUCCESS,
            payload: {},
        };

        const state = appReducer(currentState, action);

        expect(state.salesHref).toBeNull();
    });
});

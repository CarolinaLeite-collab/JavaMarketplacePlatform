import { describe, it, expect } from 'vitest';
import { render, screen } from '@testing-library/react';
import { useContext } from 'react';
import AppContext from '../context/AppContext';
import { useLibrary } from '../context/AppContext';
import { AppProvider, LibraryProvider } from '../context/AppProvider';


function ListsConsumer() {
    const { state } = useContext(AppContext);
    return <div data-testid="lists-count">{state.lists.lists.length}</div>;
}

function LibraryConsumer() {
    const { state } = useLibrary();
    return <div data-testid="items-count">{state.items.length}</div>;
}

describe('AppProvider', () => {

    it('provides initial lists state to children', () => {
        render(
            <AppProvider>
                <ListsConsumer />
            </AppProvider>
        );

        expect(screen.getByTestId('lists-count').textContent).toBe('0');
    });

});

describe('LibraryProvider', () => {

    it('provides initial library state to children', () => {
        render(
            <LibraryProvider>
                <LibraryConsumer />
            </LibraryProvider>
        );

        expect(screen.getByTestId('items-count').textContent).toBe('0');
    });

});
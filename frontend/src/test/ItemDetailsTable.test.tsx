import {render, screen} from '@/test-utils';
import { describe, it, expect, vi } from "vitest";
import {ItemDetailTable} from "../components/itemDetailsTable/ItemDetailsTable.tsx";

vi.mock('@mantine/core', async () => {
    const actual = await vi.importActual('@mantine/core');
    return {
        ...actual,
        useComputedColorScheme: () => 'dark',
    };
});

describe('ItemDetailTable', () => {

    it('always shows the type and the author', () => {
        render(
            <ItemDetailTable item={{
                publicationType: 'Book',
                authorName: 'Herbert George Wells',
                identifier: null
            }} />
        );

        expect(screen.getByText('Type')).toBeInTheDocument();
        expect(screen.getByText('Book')).toBeInTheDocument();
        expect(screen.getByText('Author')).toBeInTheDocument();
        expect(screen.getByText('Herbert George Wells')).toBeInTheDocument();
    });

    it('shows ISBN when publicationType is Book and identifier exist', () => {
        render(
            <ItemDetailTable item={{
                publicationType: 'Book',
                authorName: 'Herbert George Wells',
                identifier: '9781784872113'
            }} />
        );

        expect(screen.getByText('ISBN')).toBeInTheDocument();
        expect(screen.getByText('9781784872113')).toBeInTheDocument();
    });

    it('shows ISSN when publicationType is Magazine and identifier exist', () => {
        render(
            <ItemDetailTable item={{
                publicationType: 'Magazine',
                authorName: 'Various',
                identifier: '0027-9358'
            }} />
        );

        expect(screen.getByText('ISSN')).toBeInTheDocument();
        expect(screen.getByText('0027-9358')).toBeInTheDocument();
    });

    it('not show identifier line when identifier is null', () => {
        render(
            <ItemDetailTable item={{
                publicationType: 'Book',
                authorName: 'Old Author',
                identifier: null
            }} />
        );

        expect(screen.queryByText('ISBN')).not.toBeInTheDocument();
        expect(screen.queryByText('ISSN')).not.toBeInTheDocument();
        expect(screen.queryByText('Identifier')).not.toBeInTheDocument();
    });

    it('display the generic label when the publication type is neither a book nor a Magazine', () => {
        render(
            <ItemDetailTable item={{
                publicationType: 'Journal',
                authorName: 'Autor',
                identifier: '1234-5678'
            }} />
        );

        expect(screen.getByText('Identifier')).toBeInTheDocument();
    });

    describe('ItemDetailTable - dark mode', () => {

        it('renders correctly in dark mode', () => {
            render(
                <ItemDetailTable item={{
                    publicationType: 'Book',
                    authorName: 'Test Author',
                    identifier: '123'
                }} />
            );

            expect(screen.getByText('Author')).toBeInTheDocument();
            expect(screen.getByText('Test Author')).toBeInTheDocument();
            expect(screen.getByText('ISBN')).toBeInTheDocument();
            expect(screen.getByText('123')).toBeInTheDocument();
        });

    });



});
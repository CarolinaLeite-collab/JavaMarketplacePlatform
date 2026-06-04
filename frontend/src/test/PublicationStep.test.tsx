import { render, screen } from '../test-utils';
import userEvent from '@testing-library/user-event';
import { describe, expect, it, vi } from 'vitest';
import { PublicationStep } from '../components/addItemModal/PublicationStep';

describe('PublicationStep', () => {
    const data = {
        title: '',
        authorName: '',
        releaseYear: 0,
        genreName: '',
    };

    const authors = [
        { value: 'AUTHOR-001', label: 'Robert C. Martin' },
    ];

    const genres = [
        { value: 'GENRE-001', label: 'Software Engineering' },
    ];

    const renderPublicationStep = (setData = vi.fn()) => {
        render(
            <PublicationStep
                data={data}
                setData={setData}
                authors={authors}
                genres={genres}
            />
        );

        return { setData };
    };

    it('renders all publication fields', () => {
        renderPublicationStep();

        expect(screen.getByLabelText(/title/i)).toBeInTheDocument();
        expect(screen.getByRole('combobox', { name: /author/i })).toBeInTheDocument();
        expect(screen.getByLabelText(/release year/i)).toBeInTheDocument();
        expect(screen.getByRole('combobox', { name: /genre/i })).toBeInTheDocument();
    });

    it('updates title when user types', async () => {
        const user = userEvent.setup();
        const setData = vi.fn();

        renderPublicationStep(setData);

        await user.type(screen.getByLabelText(/title/i), 'Clean Code');

        expect(setData).toHaveBeenCalled();
    });

    it('updates release year when user types', async () => {
        const user = userEvent.setup();
        const setData = vi.fn();

        renderPublicationStep(setData);

        await user.clear(screen.getByLabelText(/release year/i));
        await user.type(screen.getByLabelText(/release year/i), '2008');

        expect(setData).toHaveBeenCalled();
    });

    it('updates author when user selects an option', async () => {
        const user = userEvent.setup();
        const setData = vi.fn();

        renderPublicationStep(setData);

        await user.click(screen.getByRole('combobox', { name: /author/i }));
        await user.click(await screen.findByText('Robert C. Martin'));

        expect(setData).toHaveBeenCalled();
    });

    it('updates genre when user selects an option', async () => {
        const user = userEvent.setup();
        const setData = vi.fn();

        renderPublicationStep(setData);

        await user.click(screen.getByRole('combobox', { name: /genre/i }));
        await user.click(await screen.findByText('Software Engineering'));

        expect(setData).toHaveBeenCalled();
    });
});
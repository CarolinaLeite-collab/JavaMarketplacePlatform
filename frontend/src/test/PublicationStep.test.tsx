import { render, screen } from '@/test-utils';
import userEvent from '@testing-library/user-event';
import { PublicationStep } from './PublicationStep';

describe('PublicationStep', () => {
    const data = {
        title: '',
        authorName: '',
        releaseYear: 0,
        genreName: '',
    };

    it('renders all publication fields', () => {
        render(<PublicationStep data={data} setData={vi.fn()} />);

        expect(screen.getByLabelText(/title/i)).toBeInTheDocument();
        expect(screen.getByLabelText(/author/i)).toBeInTheDocument();
        expect(screen.getByLabelText(/release year/i)).toBeInTheDocument();
        expect(screen.getByLabelText(/genre/i)).toBeInTheDocument();
    });

    it('updates title when user types', async () => {
        const user = userEvent.setup();
        const setData = vi.fn();

        render(<PublicationStep data={data} setData={setData} />);

        await user.type(screen.getByLabelText(/title/i), 'Clean Code');

        expect(setData).toHaveBeenCalled();
    });

    it('updates genre when user selects an option', async () => {
        const user = userEvent.setup();
        const setData = vi.fn();

        render(<PublicationStep data={data} setData={setData} />);

        await user.click(screen.getByLabelText(/genre/i));
        await user.click(screen.getByText(/science fiction/i));

        expect(setData).toHaveBeenCalled();
    });
});
import { render, screen } from '@/test-utils';
import userEvent from '@testing-library/user-event';
import { ItemStep } from '../components/addItemModal/ItemStep';

describe('ItemStep', () => {
    const data = {
        condition: '',
        description: '',
        picture: '',
    };

    it('renders all item fields', () => {
        render(<ItemStep data={data} setData={vi.fn()} />);

        expect(
            screen.getByRole('combobox', { name: /condition/i })
        ).toBeInTheDocument();
        expect(
            screen.getByLabelText(/description/i)
        ).toBeInTheDocument();
        expect(
            screen.getByLabelText(/picture url/i)
        ).toBeInTheDocument();
    });

    it('updates description when user types', async () => {
        const user = userEvent.setup();
        const setData = vi.fn();

        render(<ItemStep data={data} setData={setData} />);

        await user.type(
            screen.getByLabelText(/description/i),
            'Used copy in good condition'
        );

        expect(setData).toHaveBeenCalled();
    });

    it('updates picture url when user types', async () => {
        const user = userEvent.setup();
        const setData = vi.fn();

        render(<ItemStep data={data} setData={setData} />);

        await user.type(
            screen.getByLabelText(/picture url/i),
            'https://example.com/book.jpg'
        );

        expect(setData).toHaveBeenCalled();
    });

    it('updates condition when user selects an option', async () => {
        const user = userEvent.setup();
        const setData = vi.fn();

        render(<ItemStep data={data} setData={setData} />);

        await user.click(
            screen.getByRole('combobox', { name: /condition/i })
        );

        await user.click(
            screen.getByText('GOOD')
        );

        expect(setData).toHaveBeenCalled();
    });
});
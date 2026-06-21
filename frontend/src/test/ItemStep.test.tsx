import { render, screen } from '../test-utils';
import userEvent from '@testing-library/user-event';
import { describe, expect, it, vi } from 'vitest';
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

    it('renders no error messages when errors prop is empty', () => {
        render(<ItemStep data={data} setData={vi.fn()} errors={{}} />);

        expect(screen.queryByText(/condition is required/i)).not.toBeInTheDocument();
        expect(screen.queryByText(/description is required/i)).not.toBeInTheDocument();
    });

    it('renders inline error messages for each invalid field', () => {
        render(
            <ItemStep
                data={data}
                setData={vi.fn()}
                errors={{
                    condition: 'Condition is required',
                    description: 'Description is required',
                }}
            />
        );

        expect(screen.getByText(/condition is required/i)).toBeInTheDocument();
        expect(screen.getByText(/description is required/i)).toBeInTheDocument();
    });

    it('marks fields with errors as invalid', () => {
        render(
            <ItemStep
                data={data}
                setData={vi.fn()}
                errors={{
                    description: 'Description is required',
                }}
            />
        );

        expect(screen.getByLabelText(/description/i)).toHaveAttribute('aria-invalid', 'true');
    });
});
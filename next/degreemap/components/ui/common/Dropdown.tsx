import React from 'react'

interface Option {
    option: string
    action: () => void
}

interface DropdownProps {
    options: Option[]
    isOpen: boolean;
	onClose: () => void;
	position: { top: number; left: number };
}

export const Dropdown: React.FC<DropdownProps> = ({options, isOpen, onClose, position}) => {
    if (!isOpen) return null;
    
    return (
        <div
            className="fixed z-[1000] w-32 bg-white border rounded shadow"
            style={{
                top: `${position.top}px`,
                left: `${position.left}px`,
            }}
            onMouseLeave={onClose}
        >
            {options.map((option) => (
                <button
                    className="block w-full text-left px-4 py-2 hover:bg-gray-100"
                    onClick={() => {
                        option.action()
                        onClose()
                    }}
                >
                    {option.option}
                </button>
            ))}
        </div>
    );
}
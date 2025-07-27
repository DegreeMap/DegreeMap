import React from "react"
import { FaSearchPlus, FaSearchMinus, FaExpand, FaCog } from "react-icons/fa";

interface ToolbarProps {
    courses?: Course[]
}

export const Toolbar: React.FC<ToolbarProps> = ({courses}) => {
    const iconSize = 24
    const iconStyle = "p-2 text-gray-800 hover:text-gray-600"
    
    return (
        <div className="bg-gray-200 w-full flex p-2 items-center text-white">
            <div className="mr-auto"/>
            <button className={iconStyle}>
                <FaSearchPlus size={iconSize} />
            </button>

            <button className={iconStyle}>
                <FaSearchMinus size={iconSize} />
            </button>

            <button className={iconStyle}>
                <FaExpand size={iconSize} />
            </button>

            <button className={iconStyle}>
                <FaCog size={iconSize} />
            </button>
        </div>
    )
}
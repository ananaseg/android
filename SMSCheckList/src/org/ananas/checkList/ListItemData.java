package org.ananas.checkList;

/**
 * User: AnaNas
 * Date: 10.07.13
 * Time: 11:32
 */
public class ListItemData implements Comparable
{
    String  item;
    boolean checked;

    public ListItemData(String item, boolean checked)
    {
        this.item = item;
        this.checked = checked;
    }

    public String getItem()
    {
        return item;

    }

    public void setItem(String item)
    {
        this.item = item;
    }

    public boolean isChecked()
    {
        return checked;
    }

    public void setChecked(boolean checked)
    {
        this.checked = checked;
    }

    @Override
    public String toString()
    {
        return item;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof ListItemData))
        {
            return false;
        }

        ListItemData that = (ListItemData) o;

        if (checked != that.checked)
        {
            return false;
        }
        if (item != null ? !item.equals(that.item) : that.item != null)
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = item != null ? item.hashCode() : 0;
        result = (checked ? 1 : 0) + 31 * result;
        return result;
    }

    @Override
    public int compareTo(Object another)
    {
        if (checked == ((ListItemData) another).checked)
            return item.compareTo(((ListItemData) another).item);
        else if (checked)
            return 1;
        else
            return -1;

    }
}

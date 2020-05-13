package lb.com.network.project.model;

public class Contact implements Comparable<Contact>{
    private String uuid;
    private Double distance;

    public Double getDistance() {
        return distance == null ? 0.0D : distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public boolean equals(Object obj)
    {

        if(obj == null || obj.getClass()!= this.getClass())
            return false;

        Contact contact = (Contact) obj;


        return (contact.uuid.equals(this.uuid));
    }

    @Override
    public int hashCode()
    {
        return this.uuid.hashCode();
    }

    @Override
    public int compareTo(Contact o) {
        return getDistance().compareTo(o.getDistance());
    }
}

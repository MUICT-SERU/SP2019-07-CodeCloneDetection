@Override
public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("op=" + op).append(", keyName=" + keyName).append(", user=" + user).append(", impersonator=" + impersonator).append(", remoteHost=" + remoteHost).append(", extraMsg=" + extraMsg);
    return sb.toString();
}